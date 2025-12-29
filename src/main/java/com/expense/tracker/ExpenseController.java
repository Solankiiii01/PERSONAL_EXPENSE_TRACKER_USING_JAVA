package com.expense.tracker;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;//new
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ExpenseController {
    private List<Expense> expenses = new ArrayList<>();
    // NEW: Map to store budgets per month (e.g., "December 2025" -> 50000.0)
    private Map<String, Double> monthlyBudgets = new HashMap<>();

    @GetMapping("/")
public String index(Model model) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
    
    // Group expenses
    Map<String, Double> monthlyTotals = expenses.stream()
        .collect(Collectors.groupingBy(
            exp -> exp.getDate().format(formatter),
            Collectors.summingDouble(Expense::getAmount)
        ));

    // IMPORTANT: If monthlyBudgets is null, initialize it
    if (monthlyBudgets == null) {
        monthlyBudgets = new HashMap<>();
    }

    model.addAttribute("expenses", expenses);
    model.addAttribute("monthlyTotals", monthlyTotals);
    model.addAttribute("monthlyBudgets", monthlyBudgets); 
    model.addAttribute("total", expenses.stream().mapToDouble(Expense::getAmount).sum());
    
    return "index";
}
    @PostMapping("/set-budget")
    public String setBudget(@RequestParam String monthYear, @RequestParam Double amount) {
        // monthYear will come from a month picker (YYYY-MM)
        // We convert it to "MMMM yyyy" to match our expenses map
        LocalDate date = LocalDate.parse(monthYear + "-01");
        String formattedMonth = date.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        
        monthlyBudgets.put(formattedMonth, amount);
        return "redirect:/";
    }
    

@PostMapping("/add")
    public String addExpense(@RequestParam String category, @RequestParam Double amount, @RequestParam String date) {
        expenses.add(new Expense(System.currentTimeMillis(), category, amount, LocalDate.parse(date)));
        return "redirect:/";
}
    @PostMapping("/delete/{id}")
public String deleteExpense(@PathVariable Long id) {
    // Remove the expense where the ID matches
    expenses.removeIf(exp -> exp.getId().equals(id));
    return "redirect:/"; // Refresh the page
}

@GetMapping("/analysis")
public String showAnalysis(Model model) {
    // Group totals by Category for the Pie Chart
    Map<String, Double> categoryTotals = expenses.stream()
        .collect(Collectors.groupingBy(
            Expense::getCategory,
            Collectors.summingDouble(Expense::getAmount)
        ));

    model.addAttribute("categoryTotals", categoryTotals);
    return "analysis"; // This will be analysis.html
}
}
