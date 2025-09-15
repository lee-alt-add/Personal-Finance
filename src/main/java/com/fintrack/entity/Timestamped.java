package com.fintrack.entity;

import java.time.LocalDateTime;
import java.sql.Timestamp;


public interface Timestamped {
	Timestamp getDate();
}