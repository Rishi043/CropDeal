# Crop_Deal 

1️⃣ User Service (Authentication & Profile Management)
Features:
Farmer & Dealer Sign Up/Login 
JWT-Based Authentication
Profile Management (Update/Edit/Delete)
Role-Based Access Control (Admin, Farmer, Dealer)
Database: MySQL 

2️⃣ Crop Service (Crop Listing & Details)  
Features:
Farmers can add, edit, and delete their crop details. 
Dealers can subscribe to receive crop notifications. 
Database: MySQL  

3️⃣ Order Service (Crop Selling & Purchase) 
Features:
Dealers select a crop, create an order, and confirm the purchase.
Invoice Generation using CQRS for querying data from multiple services.
Farmers receive real-time notifications (RabbitMQ integration).
Database: MySQL 

4️⃣ Notification Service (Real-Time Updates)
Features:
Farmer gets notified when a Dealer shows interest in his crop
Dealer gets notified when a new crop is listed
with Rabbit mq with Java Mail Sender.

