CREATE TABLE IF NOT EXISTS blueprints (
    author VARCHAR(255) NOT NULL,
    name   VARCHAR(255) NOT NULL,
    points JSONB NOT NULL DEFAULT '[]'::jsonb,
    PRIMARY KEY (author, name)
);

INSERT INTO blueprints (author, name, points) VALUES
    ('john', 'house',  '[{"x":0,"y":0},{"x":10,"y":0},{"x":10,"y":10},{"x":0,"y":10}]'::jsonb),
    ('john', 'garage', '[{"x":5,"y":5},{"x":15,"y":5},{"x":15,"y":15}]'::jsonb),
    ('jane', 'garden', '[{"x":2,"y":2},{"x":3,"y":4},{"x":6,"y":7}]'::jsonb)
ON CONFLICT (author, name) DO NOTHING;
