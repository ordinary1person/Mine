#绘制极坐标函数
import turtle
import math
import random
def draw_fun(function,start,end):
    x,y = start,function(start)
    turtle.penup()
    turtle.goto(x,y)
    turtle.pendown()
    for x in range(int(start),int(end)):
        y= function(x)
        turtle.goto(x,y)
    turtle.penup()
    
def polar_to_cartesian(r, theta): 
    x = r * math.cos(theta)
    y=  r * math.sin(theta)
    return x,y

def polar_plot(r, theta):
    x,y = polar_to_cartesian(r, theta)
    turtle.goto(x,y)

a = int(random.random()*10)
draw_fun(lambda theta : a*(1-math.cos(theta)),0,300)
turtle.done()