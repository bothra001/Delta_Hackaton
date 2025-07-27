import sqlite3
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

app = FastAPI()
class UserSignupRequest(BaseModel):
    username: str
    password: str

class UserLoginRequest(BaseModel):
    username: str
    password: str

class AdminSignupRequest(BaseModel):
    adminId: str
    password: str

class AdminLoginRequest(BaseModel):
    adminId: str
    password: str

class MenuItemRequest(BaseModel):
    itemName: str
    price: float

class StallRequest(BaseModel):
    name: str
    description: str
    menu: list[MenuItemRequest]

def init_db():
    conn = sqlite3.connect("dserve.db")
    cursor = conn.cursor()
    cursor.execute("""
CREATE TABLE IF NOT EXISTS stalls (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT NOT NULL
);
""")
    cursor.execute("""
CREATE TABLE IF NOT EXISTS menu_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    itemName TEXT NOT NULL,
    price REAL NOT NULL,
    stall_id INTEGER,
    FOREIGN KEY(stall_id) REFERENCES stalls(id)
);
""")

    cursor.execute("""
        CREATE TABLE IF NOT EXISTS students (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT UNIQUE,
            password TEXT
        )
    """)
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS admins (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            adminId TEXT UNIQUE,
            password TEXT,
            secret_key TEXT
        )
    """)
    
    conn.commit()
    conn.close()

init_db()
import razorpay

client = razorpay.Client(auth=("rzp_test_1a2b3c4d5e6f7g", "1a2b3c4d5e6f7g8h9i0j1k2l"))

@app.post("/signup/user")
def signup_user(username: str, password: str):
    try:
        conn = sqlite3.connect("dserve.db")
        cursor = conn.cursor()
        cursor.execute("INSERT INTO students (username, password) VALUES (?, ?)", (username, password))
        conn.commit()
        conn.close()
        return {"message": "Student registered successfully"}
    except sqlite3.IntegrityError:
        raise HTTPException(status_code=400, detail="Username already exists")

@app.post("/signup/admin")
def signup_admin(adminId: str, password: str):
    try:
        conn = sqlite3.connect("dserve.db")
        cursor = conn.cursor
        cursor.execute("INSERT INTO admins (adminId, password) VALUES (?, ?)", (adminId, password ))
        conn.commit()
        conn.close()
        return {"message": "Admin registered successfully"}
    except sqlite3.IntegrityError:
        raise HTTPException(status_code=400, detail="Admin ID already exists")
@app.post("/login/admin")
def login_admin(adminId: str, password: str):
    conn = sqlite3.connect("dserve.db")
    cursor = conn.cursor()
    cursor.execute("SELECT password FROM admins WHERE adminId = ?", (adminId,))
    result = cursor.fetchone()
    conn.close()

    if not result:
        raise HTTPException(status_code=404, detail="Admin not found")

    if result[0] != password:
        raise HTTPException(status_code=401, detail="Incorrect password")

    return {"message": "Login successful"}
@app.post("/login/admin")
def login_admin(adminId: str, password: str):
    conn = sqlite3.connect("dserve.db")
    cursor = conn.cursor()
    cursor.execute("SELECT password FROM admins WHERE admin_id = ?", (adminId,))
    result = cursor.fetchone()
    conn.close()

    if not result:
        raise HTTPException(status_code=404, detail="Admin not found")
    if result[0] != password:
        raise HTTPException(status_code=401, detail="Incorrect password")

    return {"message": "Admin login successful"}
@app.post("/login/user")
def login_user(username: str, password: str):
    conn = sqlite3.connect("dserve.db")
    cursor = conn.cursor()
    cursor.execute("SELECT password FROM students WHERE username = ?", (username,))
    result = cursor.fetchone()
    conn.close()

    if not result:
        raise HTTPException(status_code=404, detail="User not found")
    if result[0] != password:
        raise HTTPException(status_code=401, detail="Incorrect password")

    return {"message": "User login successful"}
@app.post("/home/admin")
def add_stall(name, description, menu):
    conn = sqlite3.connect("stalls.db")
    cursor = conn.cursor()
    cursor.execute("INSERT INTO stalls (name, description) VALUES (?, ?)", (name, description))
    stall_id = cursor.lastrowid
    for item in menu:
        cursor.execute(
            "INSERT INTO menu_items (itemName, price, stall_id) VALUES (?, ?, ?)",
            (item['itemName'], item['price'], stall_id)
        )

@app.post("/create_order")
def create_order(amount: int, currency: str = "INR", receipt: str = "order_rcptid_11"):
    data = {
        "amount": amount * 100,  
        "currency": currency,
        "receipt": receipt,
        "payment_capture": 1
    }
    try:
        order = client.order.create(data=data)
        save_order_to_db(order["id"], amount)
        return {"order_id": order["id"]}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
def save_order_to_db(order_id, amount):
    conn = sqlite3.connect("payments.db")
    cursor = conn.cursor()
    cursor.execute("CREATE TABLE IF NOT EXISTS orders (order_id TEXT, amount INTEGER)")
    cursor.execute("INSERT INTO orders VALUES (?, ?)", (order_id, amount))
    conn.commit()
    conn.close()

