CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `firstname` varchar(100) NOT NULL,
  `lastname` varchar(100) NOT NULL,
  `role` varchar(50) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_active_email` (`email`,`active`),
  CONSTRAINT `user_chk_1` CHECK ((`role` in (_utf8mb4'admin',_utf8mb4'client')))
);


CREATE TABLE `address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `street` varchar(255) NOT NULL,
  `city` varchar(100) NOT NULL,
  `postcode` varchar(20) NOT NULL,
  `country` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
);


CREATE TABLE `product` (
  `productID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `creator` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `description` text NOT NULL,
  `color` varchar(50) NOT NULL,
  `stock` int NOT NULL,
  `size` varchar(20) NOT NULL,
  `imagePath` varchar(255) NOT NULL,
  `category` enum('Plushies','Clothes','Accessories','Decorations') NOT NULL,
  PRIMARY KEY (`productID`)
);


CREATE TABLE `order` (
  `orderID` int NOT NULL AUTO_INCREMENT,
  `userID` int DEFAULT NULL,
  `orderStatus` enum('In progress','Confirmed','Delivered') NOT NULL,
  `purchaseDate` datetime NOT NULL,
  `deliveryDate` datetime DEFAULT NULL,
  PRIMARY KEY (`orderID`),
  KEY `userID` (`userID`),
  CONSTRAINT `order_fk_user` FOREIGN KEY (`userID`) REFERENCES `user` (`id`) ON DELETE SET NULL
);


CREATE TABLE `orderxproduct` (
  `orderID` int NOT NULL,
  `productID` int NOT NULL,
  `quantity` int NOT NULL,
  `priceAtPurchase` decimal(10,2) NOT NULL,
  PRIMARY KEY (`orderID`,`productID`),
  KEY `orderxproduct_fk_product` (`productID`),
  CONSTRAINT `orderxproduct_fk_order` FOREIGN KEY (`orderID`) REFERENCES `order` (`orderID`) ON DELETE CASCADE,
  CONSTRAINT `orderxproduct_fk_product` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`) ON DELETE CASCADE
);


CREATE TABLE `invoice` (
  `invoiceID` int NOT NULL AUTO_INCREMENT,
  `orderID` int NOT NULL,
  `userID` int DEFAULT NULL,
  `invoiceDate` datetime NOT NULL,
  `totalAmount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`invoiceID`),
  KEY `orderID` (`orderID`),
  KEY `userID` (`userID`),
  CONSTRAINT `invoice_fk_order` FOREIGN KEY (`orderID`) REFERENCES `order` (`orderID`) ON DELETE CASCADE,
  CONSTRAINT `invoice_fk_user` FOREIGN KEY (`userID`) REFERENCES `user` (`id`) ON DELETE SET NULL
);
