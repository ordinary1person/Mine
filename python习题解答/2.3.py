import re

degrees = input("请输入角度(格式:x°x'x\"):")
degree,minute,second,kong = re.split("°|'|\"",degrees)

degree = float(degree)
minute = float(minute)
second = float(second)

degree = degree + (minute + second/60)/60
minute = degree*60 + minute + second/60
second = degree*3600 + minute*60 + second

print(
    """
    degree:%.2f°\n
    minute:%.2f'\n
    second:%.2f"      
    """
    %
    (degree,minute,second)
      )