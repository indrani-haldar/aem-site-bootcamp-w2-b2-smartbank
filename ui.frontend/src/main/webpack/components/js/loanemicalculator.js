(function () {
    "use strict";

    let loanCalculatorIndex = 0;

    const formatIndianCurrency = (value) => new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
        maximumFractionDigits: 0
    }).format(value);

    const initializeLoanCalculator = (loanCard) => {
        const form = loanCard.querySelector(".loan-card__form");
        const loanAmount = loanCard.querySelector(".loan-amount");
        const interestRate = loanCard.querySelector(".interest-rate");
        const loanTenure = loanCard.querySelector(".loan-tenure");
        const emiResult = loanCard.querySelector(".loan-card__emi");
        const errorMessage = loanCard.querySelector(".loan-card__error");
        const amountLabel = loanCard.querySelector(".loan-card__amount-label");
        const interestLabel = loanCard.querySelector(".loan-card__interest-label");
        const tenureLabel = loanCard.querySelector(".loan-card__tenure-label");

        if (!(form instanceof HTMLFormElement) ||
            !(loanAmount instanceof HTMLInputElement) ||
            !(interestRate instanceof HTMLInputElement) ||
            !(loanTenure instanceof HTMLInputElement) ||
            !(emiResult instanceof HTMLElement) ||
            !(errorMessage instanceof HTMLElement)) {
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
                emiResult.textContent = "₹ 0";
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

            if (Number.isFinite(emi)) {
                emiResult.textContent = formatIndianCurrency(emi);
            }
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
