
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
INSERT INTO migration (version, since, comment) VALUES ('1.0-SNAPSHOT', '2025-06-18 18:13:31', 'Project Start');
