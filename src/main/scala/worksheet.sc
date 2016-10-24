
def isPrime(n: Int) = n > 1 && (2 until n).forall(n % _ != 0)

isPrime(239)

def count(n: Int) = (1 until n).count(i => i % 3 == i % 5)

count(239)