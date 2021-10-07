CREATE TABLE `user_info`
(
    `id`   bigint NOT NULL,
    `name` varchar(255) NOT NULL,
    `age`  int NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;


CREATE TABLE `item`
(
    `id`    bigint  NOT NULL,
    `name`  varchar(255)    NOT NULL,
    `price` int NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;


CREATE TABLE `orders`
(
    `id`    bigint  NOT NULL,
    `user_id`   bigint NOT NULL,
    `register_time` datetime(6) NOT NULL,
    `updated_time` datetime(6) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_user_info_orders` (`user_id`),
    CONSTRAINT `FK_user_info_orders` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `order_items`
(
    `id`    bigint  NOT NULL,
    `orders_id` bigint  NOT NULL,
    `item_id`   bigint  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_orders_order_items` (`orders_id`),
    KEY `FK_item_order_items` (`item_id`),
    CONSTRAINT `FK_orders_order_items` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`),
    CONSTRAINT `FK_item_order_items` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB;
