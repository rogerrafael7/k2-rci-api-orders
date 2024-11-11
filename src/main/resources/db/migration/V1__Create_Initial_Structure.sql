create extension if not exists "uuid-ossp";

create type order_status as enum ('PENDING', 'APPROVED', 'DELIVERED', 'CANCELED');

create table orders (
    id uuid primary key default uuid_generate_v4(),
    customer_id uuid not null,
    status order_status not null default 'PENDING',
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create table order_items (
    id serial primary key,
    order_id uuid not null references orders (id),
    product_id uuid not null,
    product_unity_value numeric(10, 2) not null,
    quantity integer not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);