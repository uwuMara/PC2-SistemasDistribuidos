# Sistema Distribuido Sakila - Módulo M4

Sistema distribuido desarrollado para la gestión transaccional y financiera de alquileres de películas utilizando arquitectura cliente-servidor.

## Integrantes

* Xiomara Rivera

---

# Descripción del Proyecto

El proyecto implementa un sistema distribuido basado en:

* Cliente Java Swing
* API REST con FastAPI
* Base de datos PostgreSQL en Supabase
* Despliegue en VPS de Google Cloud

El sistema permite gestionar:

* alquileres
* devoluciones
* pagos
* control de stock
* reportes analíticos
* deudas de clientes
* autenticación de staff

---

# Arquitectura del Sistema

```txt
Java Swing Client
        ↓ HTTP REST
FastAPI VPS (Google Cloud)
        ↓
Supabase PostgreSQL
```

---

# Tecnologías Utilizadas

## Backend

* Python 3
* FastAPI
* SQLAlchemy
* PostgreSQL
* Uvicorn

## Frontend

* Java 17
* Java Swing
* Maven

## Infraestructura

* Google Cloud VPS
* Supabase PostgreSQL
* GitHub

---

# Funcionalidades Implementadas

## GUI 05 - Terminal Punto de Venta (TPV)

### Funciones

* Verificación de stock
* Registro de alquileres
* Registro de devoluciones
* Consulta de alquileres activos
* Alertas visuales de bloqueo
* Cálculo de tiempo transcurrido

### Características técnicas

* Invoca `inventory_in_stock`
* Actualiza `return_date`
* Registro transaccional distribuido
* Gestión de sesión de staff

---

## GUI 06 - Caja, Facturación y Reportes

### Funciones

* Registro de pagos
* Consulta de ingresos diarios
* Reporte de clientes destacados
* Desglose de deuda de clientes
* Filtros analíticos dinámicos

### Características técnicas

* Inserción en tabla `payment`
* Uso de `staff_id` de sesión
* Consumo del procedimiento analítico `rewards_report`
* Spinners para filtros mínimos

---

# Login de Staff

El sistema implementa autenticación de empleados utilizando la tabla `staff`.

## Usuarios de prueba

### Staff 1

```txt
Usuario: Mike
Contraseña: 12345
```

### Staff 2

```txt
Usuario: Jon
Contraseña: 12345
```

---

# Endpoints Principales

## Autenticación

```http
POST /auth/login
```

## Inventario

```http
GET /inventory/{inventory_id}/stock
```

## Alquileres

```http
POST /rentals
PUT /rentals/return
GET /rentals/active
```

## Pagos

```http
POST /payments
GET /payments/daily-income
```

## Reportes

```http
POST /reports/rewards
GET /customers/{customer_id}/debt
```

---

# Ejecución del Backend

## Crear entorno virtual

```bash
python -m venv venv
```

## Activar entorno

### Windows

```bash
venv\Scripts\activate
```

### Linux

```bash
source venv/bin/activate
```

## Instalar dependencias

```bash
pip install -r requirements.txt
```

## Ejecutar FastAPI

```bash
uvicorn app.main:app --reload
```

---

# Ejecución del Cliente Java

## Compilar

```bash
mvn clean compile
```

## Ejecutar

```bash
mvn exec:java "-Dexec.mainClass=com.sakila.Main"
```

---

# Despliegue VPS

El backend fue desplegado en una máquina virtual Ubuntu utilizando Google Cloud Platform.

## Servidor

* Ubuntu 22.04
* Python 3
* Uvicorn
* FastAPI

---

# Características Distribuidas

* Arquitectura cliente-servidor
* Comunicación HTTP REST
* Base de datos en la nube
* Backend desplegado en VPS
* Sistema desacoplado
* Gestión distribuida de transacciones

---

# Estado del Proyecto

Proyecto funcional y desplegado correctamente.

Incluye:

* frontend distribuido
* backend REST
* despliegue cloud
* autenticación
* reportes analíticos
* operaciones transaccionales
* integración PostgreSQL

---

# Autor

Proyecto desarrollado para el curso de Sistemas Distribuidos.

