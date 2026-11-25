# Warranty & Repair History Management System (Warranty Desk)

A staff-operated **Java Swing** desktop application connected to **MySQL 8** via **JDBC (MySQL Connector/J)** for managing customers, registered products, warranty coverage, repair requests, and product repair history.

---

## Project Features

- **Dashboard**: Live database-driven counts (Total Customers, Total Products, Active Warranties, Expired Warranties, Pending Repairs, Completed Repairs) and a Recent Repairs table.
- **Customer Management**: Register, search (by name or phone), update, and delete customers with 10-digit phone and optional email validation.
- **Product Management**: Register products linked to existing customers, search by product name or serial number, update details, and delete products (with unique serial number and non-future purchase date checks).
- **Warranty Tracking**: Automatically calculates warranty expiry dates (`purchase_date + warranty_years`) and `ACTIVE` / `EXPIRED` status, with search by serial number, product, or customer.
- **Repair Management**: Register repair requests with automatic warranty check (`Under Warranty` / `Out of Warranty`), search by serial number, update status (`PENDING`, `IN_PROGRESS`, `COMPLETED`), cost, and remarks, or delete selected repairs.
- **Repair History**: View all repairs or filter by product serial number to inspect product/warranty details, total repair count, total repair cost, and latest repair status.

---

## Technologies Used

- **Java** (JDK 17 or higher)
- **Java Swing** (Desktop GUI)
- **JDBC** (`DriverManager`, `Connection`, `PreparedStatement`, `ResultSet`)
- **MySQL 8**
- **MySQL Connector/J** (`com.mysql:mysql-connector-j`)
- **Maven** (`pom.xml`, optional for dependency/build management)

---

## Project Structure

```text
WarrantyRepairSystem/
├── database/
│   └── schema.sql
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── warrantyrepair/
│                   └── Main.java
├── .env.example
├── .gitignore
├── pom.xml
└── README.md
```

---

## Prerequisites

1. **Java JDK 17+** installed and available in `PATH` (`java -version`, `javac -version`).
2. **MySQL Server 8.0** installed and running locally on `localhost:3306` (Windows Service `MySQL80` = **Running**).
3. **MySQL Connector/J JDBC Driver** — **not** stored in Git (`lib/` is ignored). Obtain it automatically via **Maven** (Option A below) or download the JAR into `lib\` for direct JDK execution (Option B below).

---

## Setup Instructions (Fresh Clone)

### 1. Clone and Open the Project
```powershell
git clone <your-repository-url>
cd WarrantyRepairSystem
```

### 2. Create the Database & Tables
1. Open **MySQL Workbench** (or MySQL Command Line Client) and connect to your local MySQL 8 server.
2. Open and execute `database/schema.sql`.
   - This creates the `warranty_repair_db` database, the `customers`, `products`, and `repairs` tables, and optional demo records.
   - *(Note: Running the demo `INSERT` statements more than once on an existing database will fail on unique serial numbers or insert duplicate demo customers.)*

### 3. Configure Database Credentials (`.env`)
1. Copy `.env.example` to `.env` in the project root folder:
   ```powershell
   Copy-Item .env.example .env
   ```
2. Open `.env` and set your local MySQL credentials:
   ```env
   DB_URL=jdbc:mysql://localhost:3306/warranty_repair_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   DB_USER=root
   DB_PASSWORD=your_mysql_password_here
   ```
   - `.env` is listed in `.gitignore` so local credentials are never committed to Git.
   - As an alternative fallback, environment variables (`DB_URL`/`WARRANTY_DB_URL`, `DB_USER`/`WARRANTY_DB_USER`, `DB_PASSWORD`/`WARRANTY_DB_PASSWORD`) are also supported.

---

## How to Run

### Option A: Run with Maven (Recommended — Auto-Downloads JDBC Driver)
`pom.xml` is configured for Java 17, `com.mysql:mysql-connector-j:9.5.0`, and `exec-maven-plugin:3.5.0` with main class `com.warrantyrepair.Main`. If **Maven 3.x+** (`mvn`) is installed:

```powershell
mvn clean compile
mvn exec:java
```

### Option B: Compile and Run Directly with JDK on Windows (Without Maven)
Because `lib/` and `out/` are excluded from Git, download the MySQL Connector/J JAR into `lib\` first, then compile and run:

**PowerShell:**
```powershell
New-Item -ItemType Directory -Force -Path lib, out | Out-Null
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar" -OutFile "lib\mysql-connector-j-8.4.0.jar"
javac -d out -cp "lib\*" src\main\java\com\warrantyrepair\Main.java
java -cp "out;lib\*" com.warrantyrepair.Main
```

*(Alternatively, download the platform-independent ZIP from [MySQL Connector/J Downloads](https://dev.mysql.com/downloads/connector/j/) and copy `mysql-connector-j-*.jar` into `lib\`.)*

**Command Prompt (`cmd.exe`):**
```cmd
if not exist lib mkdir lib
if not exist out mkdir out
curl -L -o lib\mysql-connector-j-8.4.0.jar https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar
javac -d out -cp "lib\*" src\main\java\com\warrantyrepair\Main.java
java -cp "out;lib\*" com.warrantyrepair.Main
```

---

## Notes
- Enter dates in `YYYY-MM-DD` format.
- Foreign-key constraints prevent deleting customers with registered products or products with existing repair records.
- If MySQL is stopped or `.env` credentials are missing/incorrect, the application displays a **Database Connection Error** notice while keeping the UI accessible.
