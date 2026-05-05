# Class Diagram – ATM System

## What it shows
The static structure of the ATM System, including all classes, their attributes, methods, and relationships.

## Classes

**Customer**
- Attributes: customerId: String, name: String, pin: String
- Methods: insertCard(), enterPIN(), selectTransaction()

**ATMCard**
- Attributes: cardNumber: String, expiryDate: String
- Methods: validate(), getCardNumber()

**ATM**
- Attributes: atmId: String, location: String, cashAvailable: double
- Methods: readCard(), dispenseCash(), printReceipt(), ejectCard()

**BankServer**
- Attributes: serverUrl: String
- Methods: validateCard(), validatePIN(), processTransaction()

**Account**
- Attributes: accountId: String, balance: double, accountType: String
- Methods: getBalance(), deposit(), withdraw(), updateBalance()

**Transaction**
- Attributes: transactionId: String, amount: double, date: String, type: String
- Methods: execute(), getReceipt()

## Relationships
| Relationship | Type | Multiplicity |
|-------------|------|-------------|
| Customer – ATMCard | Association | 1 to 1..* |
| ATM – BankServer | Association | 1 to 1 |
| BankServer – Account | Association | 1 to 1..* |
| Account – Transaction | Aggregation | 1 to 0..* |
| ATM – Transaction | Dependency | 1 to 1 |

## Key Concepts Used
- Classes (rectangles with 3 sections)
- Attributes and methods per class
- Association, aggregation, dependency
- Multiplicity (1, 1..*, 0..*)
