CREATE TABLE IF NOT EXISTS `blocklog_kills` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`victem` varchar(75) NOT NULL,
	`killer` varchar(75) NOT NULL,
	`world` varchar(75) NOT NULL,
	`x` int(11) NOT NULL,
	`y` int(11) NOT NULL,
	`z` int(11) NOT NULL,
	`date` int(11) NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;