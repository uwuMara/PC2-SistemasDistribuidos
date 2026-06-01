import { useState } from "react";
import "./App.css";

const API = "http://34.60.83.185:8000";

function App() {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [staff, setStaff] = useState(null);

  const [result, setResult] = useState("");

  const [customerId, setCustomerId] = useState("");
  const [inventoryId, setInventoryId] = useState("");
  const [rentalId, setRentalId] = useState("");
  const [amount, setAmount] = useState("");

  async function login() {

    try {

      const response = await fetch(`${API}/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          username,
          password
        })
      });

      if (!response.ok) {
        throw new Error();
      }

      const data = await response.json();

      setStaff(data);

      setResult(`
Login correcto

Bienvenido:
${data.first_name} ${data.last_name}

Staff ID:
${data.staff_id}
      `);

    } catch {

      setResult(`
Error de autenticación

Credenciales inválidas
      `);
    }
  }

  async function checkStock() {

    try {

      const response = await fetch(
        `${API}/inventory/${inventoryId}/stock`
      );

      const data = await response.json();

      setResult(`
ESTADO DE INVENTARIO

Película:
${data.title}

Disponible:
${data.available ? "Sí" : "No"}

Inventory ID:
${inventoryId}
      `);

    } catch {

      setResult("Error verificando inventario");
    }
  }

  async function createRental() {

    try {

      const response = await fetch(`${API}/rentals`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          customer_id: Number(customerId),
          inventory_id: Number(inventoryId),
          staff_id: staff.staff_id
        })
      });

      const data = await response.json();

      setResult(`
ALQUILER REGISTRADO

Rental ID:
${data.rental_id}

Cliente:
${customerId}

Inventory:
${inventoryId}

Operador:
${staff.first_name}
      `);

    } catch {

      setResult("Error registrando alquiler");
    }
  }

  async function createPayment() {

    try {

      await fetch(`${API}/payments`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          customer_id: Number(customerId),
          staff_id: staff.staff_id,
          rental_id: Number(rentalId),
          amount: Number(amount)
        })
      });

      setResult(`
PAGO REGISTRADO

Cliente:
${customerId}

Rental:
${rentalId}

Monto:
S/ ${amount}

Operador:
${staff.first_name}
      `);

    } catch {

      setResult("Error registrando pago");
    }
  }

  if (!staff) {

    return (
      <div className="login-container">

        <div className="card login-card">

          <h1>Sakila M4</h1>

          <p className="subtitle">
            Sistema Distribuido
          </p>

          <input
            placeholder="Usuario"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />

          <input
            type="password"
            placeholder="Contraseña"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <button onClick={login}>
            Ingresar
          </button>

          <pre>{result}</pre>

        </div>

      </div>
    );
  }

  return (

    <div className="app">

      <div className="sidebar">

        <h2>Sakila Dashboard</h2>

        <p>
          Staff:
          <br />
          {staff.first_name} {staff.last_name}
        </p>

      </div>

      <div className="content">

        <div className="card">

          <h3>Verificar Stock</h3>

          <input
            placeholder="Inventory ID"
            value={inventoryId}
            onChange={(e) => setInventoryId(e.target.value)}
          />

          <button onClick={checkStock}>
            Verificar
          </button>

        </div>

        <div className="card">

          <h3>Registrar Alquiler</h3>

          <input
            placeholder="Customer ID"
            value={customerId}
            onChange={(e) => setCustomerId(e.target.value)}
          />

          <input
            placeholder="Inventory ID"
            value={inventoryId}
            onChange={(e) => setInventoryId(e.target.value)}
          />

          <button onClick={createRental}>
            Registrar
          </button>

        </div>

        <div className="card">

          <h3>Registrar Pago</h3>

          <input
            placeholder="Customer ID"
            value={customerId}
            onChange={(e) => setCustomerId(e.target.value)}
          />

          <input
            placeholder="Rental ID"
            value={rentalId}
            onChange={(e) => setRentalId(e.target.value)}
          />

          <input
            placeholder="Monto"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
          />

          <button onClick={createPayment}>
            Registrar Pago
          </button>

        </div>

        <div className="card result-card">

          <h3>Resultados</h3>

          <pre>{result}</pre>

        </div>

      </div>

    </div>
  );
}

export default App;