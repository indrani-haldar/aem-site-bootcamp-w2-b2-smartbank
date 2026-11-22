(function () {
    "use strict";

    let loanCalculatorIndex = 0;

    const formatIndianCurrency = (value) => new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(value);

    const initializeLoanCalculator = (loanCard) => {
        const form = loanCard.querySelector(".loan-card__form");
        const loanAmount = loanCard.querySelector(".loan-amount");
        const interestRate = loanCard.querySelector(".interest-rate");
        const loanTenure = loanCard.querySelector(".loan-tenure");
        const emiResult = loanCard.querySelector(".loan-card__emi");
        const totalInterestResult = loanCard.querySelector(".loan-card__total-interest");
        const totalRepaymentResult = loanCard.querySelector(".loan-card__total-repayment");
        const errorMessage = loanCard.querySelector(".loan-card__error");
        const amountLabel = loanCard.querySelector(".loan-card__amount-label");
        const interestLabel = loanCard.querySelector(".loan-card__interest-label");
        const tenureLabel = loanCard.querySelector(".loan-card__tenure-label");

        if (!(form instanceof HTMLFormElement) ||
            !(loanAmount instanceof HTMLInputElement) ||
            !(interestRate instanceof HTMLInputElement) ||
            !(loanTenure instanceof HTMLInputElement) ||
            !(emiResult instanceof HTMLElement) ||
            !(totalInterestResult instanceof HTMLElement) ||
            !(totalRepaymentResult instanceof HTMLElement) ||
            !(errorMessage instanceof HTMLElement)) {
            if (window.console && typeof window.console.error === "function") {
                window.console.error(
                    "Loan calculator could not initialize because required fields are missing.",
                    loanCard
                );
            }
            return;
        }

        const fieldPrefix = `loan-calculator-${loanCalculatorIndex++}`;
        const fields = [
            [loanAmount, amountLabel, "loan-amount"],
            [interestRate, interestLabel, "interest-rate"],
            [loanTenure, tenureLabel, "loan-tenure"]
        ];

        fields.forEach(([input, label, suffix]) => {
            if (input instanceof HTMLInputElement &&
                label instanceof HTMLLabelElement) {
                const fieldId = `${fieldPrefix}-${suffix}`;
                input.id = fieldId;
                label.htmlFor = fieldId;
            }
        });

        form.addEventListener("submit", (event) => {
            event.preventDefault();

            const principal = parseFloat(loanAmount.value);
            const annualInterest = parseFloat(interestRate.value);
            const tenureYears = parseFloat(loanTenure.value);

            if (!Number.isFinite(principal) || !Number.isFinite(annualInterest) ||
                !Number.isFinite(tenureYears) || principal <= 0 ||
                annualInterest < 0 || tenureYears <= 0) {
                errorMessage.style.display = "block";
                errorMessage.textContent = "Please enter valid loan details.";
                emiResult.textContent = formatIndianCurrency(0);
                totalInterestResult.textContent = formatIndianCurrency(0);
                totalRepaymentResult.textContent = formatIndianCurrency(0);
                return;
            }

            errorMessage.style.display = "none";

            const numberOfMonths = tenureYears * 12;
            const monthlyInterestRate = annualInterest / 12 / 100;
            let emi;

            if (monthlyInterestRate === 0) {
                emi = principal / numberOfMonths;
            } else {
                const compoundFactor = Math.pow(1 + monthlyInterestRate, numberOfMonths);
                emi = principal * monthlyInterestRate * compoundFactor /
                    (compoundFactor - 1);
            }

            const exactTotalRepayment = emi * numberOfMonths;
            const exactTotalInterest = Math.max(0, exactTotalRepayment - principal);
            const roundedEmi = Math.round(emi);
            const totalInterest = Math.round(exactTotalInterest);
            const totalRepayment = Math.round(exactTotalRepayment);

            if (!Number.isFinite(roundedEmi) || !Number.isFinite(totalRepayment) ||
                !Number.isFinite(totalInterest)) {
                errorMessage.style.display = "block";
                errorMessage.textContent = "Please enter valid loan details.";
                emiResult.textContent = formatIndianCurrency(0);
                totalInterestResult.textContent = formatIndianCurrency(0);
                totalRepaymentResult.textContent = formatIndianCurrency(0);
                return;
            }

            emiResult.textContent = formatIndianCurrency(roundedEmi);
            totalInterestResult.textContent = formatIndianCurrency(totalInterest);
            totalRepaymentResult.textContent = formatIndianCurrency(totalRepayment);
        });
    };

    const initializeLoanCalculators = () => {
        document.querySelectorAll("[data-loan-calculator]").forEach((loanCard) => {
            if (loanCard instanceof HTMLElement) {
                initializeLoanCalculator(loanCard);
            }
        });
    };

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initializeLoanCalculators, { once: true });
    } else {
        initializeLoanCalculators();
    }
})();
