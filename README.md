Inventory App - Kevin Simmons

CS 360 Mobile Architecture & Programming | SNHU

Overview

The Inventory App is a fully functional Android application that allows users to track and manage inventory items. Users create a secure account, log in, and perform full create, read, update, and delete (CRUD) operations on their inventory. All data is stored locally in a persistent SQLite database, and the app can optionally send SMS notifications when an item's stock runs low.

Features


User authentication — create an account and log in, with credentials checked against a SQLite database
Persistent storage — all user and inventory data is saved in a local SQLite database that survives app restarts
Full CRUD operations:

Create — add new inventory items with a name and quantity
Read — view all items in a scrollable grid
Update — increase or decrease item quantities with one tap
Delete — remove items from the inventory



Low-stock SMS alerts — sends a text message notification when an item drops below the low-stock threshold (requires SEND_SMS permission)
Graceful permission handling — if SMS permission is denied, the app continues to function normally without the notification feature


Tech Stack


Language: Java
Database: SQLite (via SQLiteOpenHelper)
UI: RecyclerView grid, LinearLayout
Min SDK: API 34 (Android 14)
Target SDK: API 35
Build System: Gradle (Groovy DSL)


Project Structure

InventoryAppKevinSimmons2/
├── app/
│   ├── src/main/
│   │   ├── java/com/kevinsimmons/inventoryappkevinsimmons/
│   │   │   ├── LoginActivity.java        # Login and account creation
│   │   │   ├── InventoryActivity.java    # Main inventory screen + SMS logic
│   │   │   ├── InventoryAdapter.java     # RecyclerView adapter for the grid
│   │   │   ├── InventoryItem.java         # Data model for an inventory item
│   │   │   └── DatabaseHelper.java        # SQLite database (users + inventory)
│   │   ├── res/layout/
│   │   │   ├── activity_login.xml         # Login screen layout
│   │   │   ├── activity_inventory.xml     # Inventory grid layout
│   │   │   └── item_inventory_row.xml     # Single row layout for the grid
│   │   └── AndroidManifest.xml
│   └── build.gradle
└── README.md

Database Design

The app uses a single SQLite database (InventoryApp.db) containing two tables:

users

ColumnTypeNotesidINTEGERPrimary key, auto-incrementusernameTEXTUnique, not nullpasswordTEXTNot null

inventory

ColumnTypeNotesidINTEGERPrimary key, auto-incrementitem_nameTEXTNot nullquantityINTEGERNot null

How It Works


The user opens the app and is presented with the login screen.
A new user taps Create Account to register; their username and password are saved to the database.
The user logs in, and their credentials are validated against the database.
On success, the inventory screen loads and displays all items in a grid.
The user can add items, adjust quantities with the +/- buttons, or delete items. Every change is written to the database immediately.
When an item's quantity falls below the low-stock threshold, the app sends an SMS alert (if the user granted SMS permission).
If the user denied SMS permission, all other features keep working normally.


Permissions


SEND_SMS — used only to send low-stock alert notifications. This permission is optional; the app remains fully functional if it is denied.


Course Information


Course: CS 360 - Mobile Architecture & Programming
School: Southern New Hampshire University (SNHU)
Student: Kevin Simmons
