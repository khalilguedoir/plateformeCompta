package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "timezones", schema = "public")
public class Timezone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tz_name")
    private String tzName;

    @Column(name = "utc_offset")
    private String utcOffset;

    @Column(name = "is_dst")
    private Boolean isDst;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTzName() {
		return tzName;
	}

	public void setTzName(String tzName) {
		this.tzName = tzName;
	}

	public String getUtcOffset() {
		return utcOffset;
	}

	public void setUtcOffset(String utcOffset) {
		this.utcOffset = utcOffset;
	}

	public Boolean getIsDst() {
		return isDst;
	}

	public void setIsDst(Boolean isDst) {
		this.isDst = isDst;
	}
    
    
}
