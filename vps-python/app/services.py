from sqlalchemy import text
from sqlalchemy.orm import Session
from datetime import datetime
import hashlib


def inventory_in_stock(db: Session, inventory_id: int):

    result = db.execute(text("""
        SELECT inventory_in_stock(:inventory_id)
    """), {
        "inventory_id": inventory_id
    }).scalar()

    return bool(result)


def inventory_detail(db: Session, inventory_id: int):

    row = db.execute(text("""
        SELECT
            i.inventory_id,
            f.title,
            inventory_in_stock(i.inventory_id) AS available
        FROM inventory i
        INNER JOIN film f ON f.film_id = i.film_id
        WHERE i.inventory_id = :inventory_id
    """), {
        "inventory_id": inventory_id
    }).mappings().first()

    if row is None:
        raise Exception("No existe ese inventario.")

    return dict(row)


def create_rental(db: Session, customer_id: int, inventory_id: int, staff_id: int):

    stock = inventory_in_stock(db, inventory_id)

    if not stock:
        raise Exception("La película no está disponible.")

    rental = db.execute(text("""
        INSERT INTO rental (
            rental_date,
            inventory_id,
            customer_id,
            return_date,
            staff_id
        )
        VALUES (
            NOW(),
            :inventory_id,
            :customer_id,
            NULL,
            :staff_id
        )
        RETURNING rental_id
    """), {
        "inventory_id": inventory_id,
        "customer_id": customer_id,
        "staff_id": staff_id
    }).fetchone()

    db.commit()

    return {
        "message": "Alquiler registrado correctamente",
        "rental_id": rental.rental_id
    }


def return_rental(db: Session, rental_id: int):

    db.execute(text("""
        UPDATE rental
        SET return_date = NOW()
        WHERE rental_id = :rental_id
    """), {
        "rental_id": rental_id
    })

    db.commit()

    return {
        "message": "Devolución registrada correctamente"
    }


def active_rentals(db: Session):

    rentals = db.execute(text("""
        SELECT
            r.rental_id,
            c.first_name || ' ' || c.last_name AS customer_name,
            f.title AS film_title,
            r.inventory_id,
            TO_CHAR(r.rental_date, 'YYYY-MM-DD HH24:MI') AS rental_date,
            EXTRACT(DAY FROM NOW() - r.rental_date)::INT AS dias,
            EXTRACT(HOUR FROM NOW() - r.rental_date)::INT AS horas,
            EXTRACT(MINUTE FROM NOW() - r.rental_date)::INT AS minutos
        FROM rental r
        INNER JOIN customer c ON c.customer_id = r.customer_id
        INNER JOIN inventory i ON i.inventory_id = r.inventory_id
        INNER JOIN film f ON f.film_id = i.film_id
        WHERE r.return_date IS NULL
        ORDER BY r.rental_date DESC
        LIMIT 10
    """)).mappings().all()

    return rentals


def create_payment(
    db: Session,
    customer_id: int,
    staff_id: int,
    rental_id: int,
    amount: float
):

    db.execute(text("""
        INSERT INTO payment (
            customer_id,
            staff_id,
            rental_id,
            amount,
            payment_date
        )
        VALUES (
            :customer_id,
            :staff_id,
            :rental_id,
            :amount,
            NOW()
        )
    """), {
        "customer_id": customer_id,
        "staff_id": staff_id,
        "rental_id": rental_id,
        "amount": amount
    })

    db.commit()

    return {
        "message": "Pago registrado correctamente"
    }


def daily_income(db: Session):

    result = db.execute(text("""
        SELECT COALESCE(SUM(amount),0)
        FROM payment
        WHERE DATE(payment_date) = CURRENT_DATE
    """)).scalar()

    return {
        "fecha": str(datetime.now().date()),
        "ingresos_totales": float(result)
    }

def rewards_report(
    db: Session,
    min_payments: int,
    min_amount: float
):

    report = db.execute(text("""
        SELECT
            c.customer_id,
            c.first_name,
            c.last_name,
            COUNT(p.payment_id) AS total_pagos,
            SUM(p.amount) AS total_gastado
        FROM payment p
        JOIN customer c
            ON p.customer_id = c.customer_id
        GROUP BY
            c.customer_id,
            c.first_name,
            c.last_name
        HAVING COUNT(p.payment_id) >= :min_payments
        AND SUM(p.amount) >= :min_amount
        ORDER BY total_gastado DESC
    """), {
        "min_payments": min_payments,
        "min_amount": min_amount
    }).mappings().all()

    return report

def customer_debt(db: Session, customer_id: int):

    row = db.execute(text("""
        SELECT
            c.customer_id,
            c.first_name || ' ' || c.last_name AS customer_name,
            COUNT(r.rental_id) AS alquileres_activos,

            COALESCE(
                MAX(
                    EXTRACT(DAY FROM NOW() - r.rental_date)::INT
                ),
                0
            ) AS dias_alquilado,
            COALESCE(SUM(
                CASE
                    WHEN r.rental_id IS NOT NULL THEN
                        GREATEST(
                            EXTRACT(DAY FROM NOW() - r.rental_date)::INT,
                            1
                        ) * 2.99
                    ELSE 0
                END
            ), 0) AS monto_estimado
        FROM customer c
        LEFT JOIN rental r
            ON r.customer_id = c.customer_id
            AND r.return_date IS NULL
        WHERE c.customer_id = :customer_id
        GROUP BY c.customer_id, customer_name
    """), {
        "customer_id": customer_id
    }).mappings().first()

    if row is None:
        raise Exception("Cliente no encontrado.")

    return dict(row)


def login_staff(db: Session, username: str, password: str):

    password_hash = hashlib.sha1(
        password.encode()
    ).hexdigest()

    row = db.execute(text("""
        SELECT
            staff_id,
            first_name,
            last_name,
            username
        FROM staff
        WHERE username = :username
        AND password = :password
        AND active = true
    """), {
        "username": username,
        "password": password_hash
    }).mappings().first()

    if row is None:
        raise Exception("Credenciales inválidas.")

    return dict(row)