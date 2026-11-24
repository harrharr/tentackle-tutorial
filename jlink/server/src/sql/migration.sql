
-- create the migration table (obsolete if tools like flywaydb used)
CREATE TABLE migration (
    version VARCHAR(32),
    since TIMESTAMP,
    comment VARCHAR(512)
);
COMMENT ON TABLE migration IS 'migration history';
COMMENT ON COLUMN migration.version IS 'deployment date';
COMMENT ON COLUMN migration.since IS 'deployment date';
COMMENT ON COLUMN migration.comment IS 'additional comments';
INSERT INTO migration (version, since, comment) VALUES ('1.0-SNAPSHOT', '2026-11-24 18:00:00', 'Project Start');
