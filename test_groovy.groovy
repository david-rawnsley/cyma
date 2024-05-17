def getYearlyValueInNZD(billingCycle, billingUnit, monthlyValue, userCount, transactionBudget, licenseCurrency, exchangeRates) {
  // Handle cases where billing_cycle is not provided or empty
  if (!billingCycle) {
    throw new IllegalArgumentException("Billing cycle cannot be null or empty")
  }

  def multiplier = 1
  switch (billingCycle.toLowerCase()) {
    case 'monthly':
      multiplier = 12
      break
    case 'yearly':
      multiplier = 1
      break
    default:
      throw new IllegalArgumentException("Unsupported billing cycle: $billingCycle")
  }

  def yearlyValue

  switch (billingUnit.toLowerCase()) {
    case 'flat':
      yearlyValue = licenseUnitValue * multiplier
      break
    case 'user':
      yearlyValue = licenseUnitValue * userCount * multiplier
      break
    case 'transaction':
      yearlyValue = licenseUnitValue * transactionBudget * multiplier
      break
    default:
      throw new IllegalArgumentException("Unsupported billing unit: $billingUnit")
  }

  if (licenseCurrency.toLowerCase() != 'nzd') {
    def exchangeRate = exchangeRates.get(licenseCurrency.toUpperCase())
    if (exchangeRate == null) {
      throw new IllegalArgumentException("Exchange rate not provided for currency: $licenseCurrency")
    }
    yearlyValue *= exchangeRate
  }

  return yearlyValue
}

// Example usage - replace exchangeRates map with actual values
def exchangeRates = [
  'USD': 1.69, // Replace with actual USD to NZD exchange rate
  'EUR': 1.81, // Replace with actual EUR to NZD exchange rate
  'AUD': 1.1, // Replace with actual AUD to NZD exchange rate
  'GBP': 2.11, // Replace with actual GBP to NZD exchange rate
]

// Example usage assuming your gremlin query assigns results to 'g' and maps properties to variables
def g = // Your gremlin query here

def billingCycle = g.V().get('billing_cycle') // Assuming 'billingCycle' property exists
def billingUnit = g.V().get('billing_unit') // Assuming 'billingUnit' property exists
def licenseUnitValue = g.V().get('license_cost_per_unit') // Assuming 'monthlyValue' property exists
def userCount = g.V().get('user_count') // Assuming 'userCount' property exists
def transactionBudget = g.V().get('budgeted_monthly_transaction_volume') // Assuming 'transactionBudget' property exists
def licenseCurrency = g.V().get('license_currency') // Assuming 'licenseCurrency' property exists

def yearlyValueInNZD = getYearlyValueInNZD(billingCycle, billingUnit, monthlyValue, userCount, transactionBudget, licenseCurrency, exchangeRates)
println yearlyValueInNZD

// Handle potential exceptions during gremlin query or property access
try {
  // Your gremlin query and property access code here
} catch (Exception e) {
  println "Error retrieving data: ${e.message}"
}