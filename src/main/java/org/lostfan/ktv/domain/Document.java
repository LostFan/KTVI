package org.lostfan.ktv.domain;

import java.time.LocalDate;

public interface Document extends Entity {
    LocalDate getDate();
}
