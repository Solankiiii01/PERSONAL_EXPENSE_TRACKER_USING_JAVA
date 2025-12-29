package com.expense.tracker;
import java.time.LocalDate;

public class Expense {
    private Long id;
    private String category;
    private Double amount;
    private LocalDate date; // NEW: The date of spending

    public Expense(Long id, String category, Double amount, LocalDate date) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() { return id; }
    public String getCategory() { return category; }
    public Double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
}