CREATE TABLE IF NOT EXISTS 'blocklog_blocks' (
	'id' INTEGER PRIMARY KEY NOT NULL,
	'player' VARCHAR(75) NOT NULL,
	'world' VARCHAR(75) NOT NULL,
	'block_id' INTEGER NOT NULL,
	'datavalue' INTEGER NOT NULL,
	'type' INTEGER NOT NULL,
	'rollback_id' INTEGER NOT NULL DEFAULT '0',
	'x' INTEGER NOT NULL,
	'y' INTEGER NOT NULL,
	'z' INTEGER NOT NULL,
	'date' INTEGER NOT NULL
);