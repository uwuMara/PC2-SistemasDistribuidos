from pydantic import BaseModel
from typing import Optional

class RentalCreate(BaseModel):
    customer_id: int
    inventory_id: int
    staff_id: int

class ReturnRental(BaseModel):
    rental_id: int

class PaymentCreate(BaseModel):
    customer_id: int
    staff_id: int
    rental_id: Optional[int] = None
    amount: float

class RewardsReportRequest(BaseModel):
    min_payments: int
    min_amount: float

class LoginRequest(BaseModel):
    username: str
    password: str   