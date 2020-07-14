CREATE TABLE USER_ACCOUNT 
(
    user_id NUMBER GENERATED ALWAYS AS IDENTITY,
    user_email VARCHAR2(255) UNIQUE NOT NULL,
    user_lastname VARCHAR(64) NOT NULL,
    user_firstname VARCHAR2(64) NOT NULL,
    user_phone VARCHAR2(13),
    user_password VARCHAR2(255),
    user_subscription_date DATE
)