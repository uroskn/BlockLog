CREATE TABLE IF NOT EXISTS 'blocklog_interactions' (
	'id' INTEGER PRIMARY KEY NOT NULL,
	'player' VARCHAR(75) NOT NULL,
	'world' VARCHAR(75) NOT NULL,
	'type' INTEGER NOT NULL,
	'x' INTEGER NOT NULL,
	'y' INTEGER NOT NULL,
	'z' INTEGER NOT NULL,
	'date' INTEGER NOT NULL
);