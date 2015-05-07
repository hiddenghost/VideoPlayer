print "hello"
# Given a list of [1, 2] and a list of [3, 4]
#
# The result should be [4, 5, 6].

# a = [1, 2]
# b = [3, 4]

# def f(a, b):
#     r = []
#     for itema in a:
#         for itemb in b:
#             r.append(itema + itemb)
#     r = list(set(r))
#     return r

# isSumInSequence(6) => true // (1+2+3) = 6
# isSumInSequence(7) => false // (1+2+3+4) != 7
import math
def isSumInSequence(x):
    n = int(math.sqrt(x*2))
    s = 0
    for i in range(1, n + 1):
        s += i
    if s == x:
        return True
    else:
        return False

print isSumInSequence(7)

# def f(s, n):
#     temp = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
#     newS = ""
#     for i in range(0, len(s)):
#         if s[i] in temp:
#             index = temp.index(s[i])
#             index += n
#             index = index % len(temp)
#             newS += temp[index]
#         else:
#             newS += s[i]
#     print newS

# print f("AB1Z", 3)

def f(query, title):
    threshold = len(query)
    rank = []
    for i in range(0, len(title)):
        for item in query:
            if title[i] in item:
                rank.append(query.index(item))
    rank = list(set(rank))
    value = len(rank)
    if value >= threshold:
        return True
    else:
        return False

x = [[ "A", "B"], [ "C", "D"]]
print f(x, "ACDC")
