class check(object):
    def __init__(self):
        self.card_id = input('please tell me your ID:')
        self.xishu = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]
        self.yushu = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2]
    def estimate(self):
        if len(list(self.card_id)) != 17:
            print('error!')
            return False
        return True
    def caculate(self):
        count = 0
        for i in range(17):
            count += int(self.card_id[i]) * self.xishu[i]
        last = count % 11
        print('你的身份证号码是：%s%s' % (self.card_id, self.yushu[last]))
zig = check()
b = zig.estimate()
if b:
 zig.caculate()
