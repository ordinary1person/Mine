#求四位的水仙花数
for i in range(1000,10000):
    a = int(i / 1000)
    b = int((i - a * 1000) / 100)
    c = int((i - a * 1000 - b * 100) / 10)
    d = int(i - a * 1000 - b * 100 - c * 10)
    if i == a ** 4 + b ** 4 + c ** 4 + d ** 4:
        print(i)