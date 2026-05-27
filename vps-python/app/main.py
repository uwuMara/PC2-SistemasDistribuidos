from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from sqlalchemy import text
from app.database import get_db
from app.schemas import (
    RentalCreate,
    ReturnRental,
    PaymentCreate,
    RewardsReportRequest
)

from app import services
from app.schemas import (
    RentalCreate,
    ReturnRental,
    PaymentCreate,
    RewardsReportRequest,
    LoginRequest
)

app = FastAPI(
    title="Sakila Distributed API"
)

@app.get("/")
def home():
    return {
        "message": "API Sakila funcionando"
    }

@app.get("/test-db")
def test_db(db: Session = Depends(get_db)):
    total = db.execute(
        text("SELECT COUNT(*) FROM customer")
    ).scalar()

    return {
        "clientes": total
    }

# ---------------------------------------------------
# INVENTORY
# ---------------------------------------------------

@app.get("/inventory/{inventory_id}/stock")
def check_stock(
    inventory_id: int,
    db: Session = Depends(get_db)
):

    try:
        return services.inventory_detail(
            db,
            inventory_id
        )

    except Exception as e:
        raise HTTPException(
            status_code=404,
            detail=str(e)
        )

# ---------------------------------------------------
# RENTALS
# ---------------------------------------------------

@app.post("/rentals")
def create_rental(
    data: RentalCreate,
    db: Session = Depends(get_db)
):

    try:

        return services.create_rental(
            db,
            data.customer_id,
            data.inventory_id,
            data.staff_id
        )

    except Exception as e:

        raise HTTPException(
            status_code=400,
            detail=str(e)
        )

@app.put("/rentals/return")
def return_rental(
    data: ReturnRental,
    db: Session = Depends(get_db)
):

    return services.return_rental(
        db,
        data.rental_id
    )

@app.get("/rentals/active")
def active_rentals(
    db: Session = Depends(get_db)
):

    return services.active_rentals(db)

# ---------------------------------------------------
# PAYMENTS
# ---------------------------------------------------

@app.post("/payments")
def create_payment(
    data: PaymentCreate,
    db: Session = Depends(get_db)
):

    return services.create_payment(
        db,
        data.customer_id,
        data.staff_id,
        data.rental_id,
        data.amount
    )

@app.get("/payments/daily-income")
def daily_income(
    db: Session = Depends(get_db)
):

    return services.daily_income(db)

# ---------------------------------------------------
# REPORTS
# ---------------------------------------------------

@app.post("/reports/rewards")
def rewards_report(
    data: RewardsReportRequest,
    db: Session = Depends(get_db)
):

    return services.rewards_report(
        db,
        data.min_payments,
        data.min_amount
    )

@app.get("/customers/{customer_id}/debt")
def customer_debt(
    customer_id: int,
    db: Session = Depends(get_db)
):
    try:
        return services.customer_debt(
            db,
            customer_id
        )

    except Exception as e:
        raise HTTPException(
            status_code=404,
            detail=str(e)
        )

@app.post("/auth/login")
def login(
    data: LoginRequest,
    db: Session = Depends(get_db)
):
    try:
        return services.login_staff(
            db,
            data.username,
            data.password
        )

    except Exception as e:
        raise HTTPException(
            status_code=401,
            detail=str(e)
        )