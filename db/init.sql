CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE priorite_offre AS ENUM ('elevee', 'normale', 'basse');

CREATE TABLE vol (
    id SERIAL PRIMARY KEY,
    origin VARCHAR(3) NOT NULL CHECK (length(origin) = 3),
    destination VARCHAR(3) NOT NULL CHECK (length(destination) = 3),
    aller_retour BOOLEAN NOT NULL DEFAULT false,
    prix DECIMAL(10,2) NOT NULL CHECK (prix > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_vol_origin ON vol(origin);
CREATE INDEX idx_vol_destination ON vol(destination);

CREATE TABLE offre (
    id SERIAL PRIMARY KEY,
    vol_id INTEGER NOT NULL REFERENCES vol(id) ON DELETE CASCADE,
    priorite priorite_offre NOT NULL DEFAULT 'normale',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_offre_vol_id ON offre(vol_id);
CREATE INDEX idx_offre_priorite ON offre(priorite);

INSERT INTO vol (origin, destination, aller_retour, prix) VALUES
('CDG', 'LHR', true, 150.00),
('CDG', 'AMS', true, 120.00),
('CDG', 'FRA', false, 180.00),
('CDG', 'BCN', true, 200.00),
('CDG', 'ROM', true, 250.00),
('CDG', 'JFK', true, 650.00),
('CDG', 'LAX', true, 850.00),
('CDG', 'NRT', true, 1200.00),
('CDG', 'DXB', false, 550.00),
('CDG', 'SIN', true, 950.00),
('CDG', 'CAI', true, 450.00),
('CDG', 'IST', false, 280.00),
('CDG', 'ATH', true, 320.00),
('CDG', 'NCE', true, 180.00),
('CDG', 'MRS', false, 120.00),
('CDG', 'TLS', true, 150.00),
('ORY', 'NCE', true, 170.00),
('ORY', 'TLS', false, 110.00),
('LYS', 'CDG', true, 140.00),
('MRS', 'ORY', false, 130.00);

INSERT INTO offre (vol_id, priorite) VALUES
(1, 'elevee'),
(6, 'elevee'),
(8, 'elevee'),
(4, 'normale'),
(5, 'normale'),
(10, 'normale'),
(13, 'normale'),
(14, 'normale'),
(2, 'basse'),
(3, 'basse'),
(16, 'basse'),
(17, 'basse');

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_vol_updated_at BEFORE UPDATE ON vol
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_offre_updated_at BEFORE UPDATE ON offre
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE VIEW v_offres_detail AS
SELECT 
    o.id AS offre_id,
    o.priorite,
    o.created_at AS offre_created_at,
    o.updated_at AS offre_updated_at,
    v.id AS vol_id,
    v.origin,
    v.destination,
    v.aller_retour,
    v.prix,
    v.created_at AS vol_created_at
FROM offre o
JOIN vol v ON o.vol_id = v.id
ORDER BY 
    CASE o.priorite 
        WHEN 'elevee' THEN 1 
        WHEN 'normale' THEN 2 
        WHEN 'basse' THEN 3 
    END,
    o.created_at DESC;

CREATE ROLE admin_role WITH LOGIN PASSWORD 'admin_password';
GRANT ALL PRIVILEGES ON DATABASE poc_airfrance TO admin_role;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO admin_role;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO admin_role;
GRANT USAGE ON SCHEMA public TO admin_role;

CREATE ROLE user_role WITH LOGIN PASSWORD 'user_password';
GRANT CONNECT ON DATABASE poc_airfrance TO user_role;
GRANT USAGE ON SCHEMA public TO user_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON offre TO user_role;
GRANT USAGE, SELECT ON SEQUENCE offre_id_seq TO user_role;
GRANT SELECT ON vol TO user_role;
GRANT SELECT ON v_offres_detail TO user_role;
REVOKE INSERT, UPDATE, DELETE ON vol FROM user_role;

CREATE USER admin_user WITH PASSWORD 'Admin123!';
GRANT admin_role TO admin_user;

CREATE USER dev_user WITH PASSWORD 'Dev123!';
GRANT user_role TO dev_user;
