package com.adt.expensemanagement.models;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cap_ex_details", catalog = "EmployeeDB", schema = "expense_schema")
public class CapExDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "serial")
	private int id;

	
	@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
	@Column(name = "date", nullable = false)
	private LocalDate date;

	@Column(name = "expense_details", nullable = false)
	private String expenseDetails;

	@Column(name = "gst_bill", nullable = false)
	private String gstBill;

	@Column(name = "amount", nullable = false)
	private Integer amount;

	@Column(name = "paid_by", nullable = false)
	private String paidBy;

	@Column(name = "comment", nullable = false)
	private String comment;

	@Column(name = "mode", nullable = false)
	private String mode;
	
	//HRMS-114 -> START
	@Lob
	@Column(name = "invoice" )
	private byte[] invoice;
	//HRMS-114 -> END
}
