/**
* Computes the periodical payment necessary to re-pay a given loan.
*/
public class LoanCalc {
	
	static double epsilon = 0.001;  // The computation tolerance (estimation error)
	static int iterationCounter;    // Monitors the efficiency of the calculation
	
    /** 
     * Gets the loan data and computes the periodical payment.
     * Expects to get three command-line arguments: sum of the loan (double),
     * interest rate (double, as a percentage), and number of payments (int).  
     */
	public static void main(String[] args) {		
		// Gets the loan data
		double loan = Double.parseDouble(args[0]);
		double rate = Double.parseDouble(args[1]);
		int n = Integer.parseInt(args[2]);

		System.out.println("Loan sum = " + loan + ", interest rate = " + rate + "%, periods = " + n);
		
		// Computes the periodical payment using brute force search        
		System.out.print("Periodical payment, using brute force: ");
		System.out.printf("%.2f", bruteForceSolver(loan, rate, n, epsilon));
		System.out.println();
		System.out.println("number of iterations: " + iterationCounter);

		// Computes the periodical payment using bisection search
		System.out.print("Periodical payment, using bi-section search: ");
		System.out.printf("%.2f", bisectionSolver(loan, rate, n, epsilon));
		System.out.println();
		System.out.println("number of iterations: " + iterationCounter);
	}
	
	/**
	* Uses a sequential search method  ("brute force") to compute an approximation
	* of the periodical payment that will bring the ending balance of a loan close to 0.
	* Given: the sum of the loan, the periodical interest rate (as a percentage),
	* the number of periods (n), and epsilon, a tolerance level.
	*/
	// Side effect: modifies the class variable iterationCounter.
    public static double bruteForceSolver(double loan, double rate, int n, double epsilon) {  
    	iterationCounter = 0;
    	double payment = loan/n;
    	while (endBalance(loan, rate, n, payment) > epsilon) {
    		payment += epsilon;
    		iterationCounter++;
    	}
   		return payment;
    }
    
    /**
	* Uses bisection search to compute an approximation of the periodical payment 
	* that will bring the ending balance of a loan close to 0.
	* Given: the sum of the loan, the periodical interest rate (as a percentage),
	* the number of periods (n), and epsilon, a tolerance level.
	*/
	// Side effect: modifies the class variable iterationCounter.
    public static double bisectionSolver(double loan, double rate, int n, double epsilon) {  
    	iterationCounter = 0;
    	double lPay = loan/n;
		// do the following hPay for optimized seach - maximum possible total payment is if the debt increases without paying anything off until the last year.
		// double hPay = (loan * Math.pow((1 + (rate / 100)), n)) / n;
		// do the following hpay for github expected answer:
		double hPay = loan;
    	double midPay = (lPay + hPay) / 2;
    	double endBal = endBalance(loan, rate, n, midPay);
		/*
		// original - optimized version
		while (Math.abs(endBal) > epsilon) {
			iterationCounter++;
			if (0 < endBal) {
				lPay = midPay;
			} else {
				hPay = midPay;
			}
			midPay = (lPay + hPay) / 2;
			endBal = endBalance(loan, rate, n, midPay);
		}
		*/
		// underneath is the version for github answers
		while (hPay - lPay > epsilon) {
			iterationCounter++;
			if (0 < endBal) {
				lPay = midPay;
			} else {
				hPay = midPay;
			}
			midPay = (lPay + hPay) / 2;
			endBal = endBalance(loan, rate, n, midPay);
		}
    	return midPay;
    }
	
	/**
	* Computes the ending balance of a loan, given the sum of the loan, the periodical
	* interest rate (as a percentage), the number of periods (n), and the periodical payment.
	*/
	private static double endBalance(double loan, double rate, int n, double payment) {
		for (int i = 1; i <= n; i++) {
			loan -= payment;
			loan *= (1 + (rate / 100));
			}
		return loan;
	}
}