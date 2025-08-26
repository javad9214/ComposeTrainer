# 📦 StockGuard (یارانبار)

StockGuard (یارانبار) is a **shop & warehouse inventory manager** for Android, built with **Kotlin + Jetpack Compose**. It helps small retailers track products, create invoices, and see quick sales insights—fast and offline.

---

## ✨ Features (current)
- 🧭 **Bottom Navigation**: Home · Products · Analysis · Invoices
- 🛒 **Products page**: list with quantity & price
- 🧾 **Invoices**:
  - list overview
  - **Add Invoice**: shows date/time, live total, final submit
  - add product via **BottomSheet** (basic for now; search planned)
- 📈 **Analysis**: charts with **Vico** (ongoing)

> **Data model** (Room): `InvoiceEntity`, `ProductEntity`, `InvoiceProductCrossRef`, relation `InvoiceWithProducts`.

---

## 🏗 Tech Stack
- **Language:** Kotlin
- **Architecture:** Clean Architecture + MVVM
- **DI:** Hilt
- **UI:** Jetpack Compose
- **Persistence:** Room
- **Networking:** Retrofit (planned for server sync)
- **Charts:** Vico

---

## 🚀 Getting Started

### Requirements
- Android Studio (2024.1+ recommended)
- JDK 17+
- Android SDK 24+

### Clone & Run
```bash
git clone https://github.com/your-username/StockGuard.git
cd StockGuard
