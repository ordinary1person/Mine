heigh = 0.3/1000

for i in range(0,35):
    heigh = heigh * 2
    
print("第35次对折后纸的厚度：%.2f"%heigh)
if heigh>8848.44:
    print("确实比珠穆朗玛峰高！")
else:
    print("没有珠穆朗玛峰高！")