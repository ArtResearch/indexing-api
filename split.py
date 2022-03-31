import os
import re
import sys
from fileinput import filename
from optparse import OptionParser

startToken = "CONSTRUCT"
endToken = "LIMIT 1"
dst = "default"
regexPatterns = [r'(\?.*[\s\n\t]+<https:\/\/artresearch\.net\/resource\/fr\/)(.*)>', r'(\?.*[\s\n\t]+<http:\/\/www\.cidoc-crm\.org\/cidoc-crm\/)(.*)>']

def findEndToken(table, endToken):
    storedLines = []
    fileString = table[1]
    copy = 0
    for index, line in enumerate(table):
        storedLines.append(line)                
        if endToken in line: 
            for rxpattern in regexPatterns:
                groups = re.findall(rxpattern, fileString)
                if not groups:
                    continue
                fileName = "queries/" + groups[0][1] + "_" + str(copy)
                while (os.path.exists(fileName)):
                    copy += 1                                       
                    fileName = fileName[:-1] + str(copy)
                with open(fileName, "w") as df:
                    for l in storedLines:
                        df.write(l)
            break
    return index

def findEndTokenIndexApi(table, endToken):
    storedLines = []
    copy = 0
    dstDir = dst if not dst is "default" else "queries" 
    for index, line in enumerate(table):
        storedLines.append(line)                
        if endToken in line:  
            storedLines[-1] = storedLines[-1].replace(endToken, "")           
            print("End token")
            fileName = dstDir + "/query_" + str(copy)
            while (os.path.exists(fileName)):
                copy += 1                                       
                fileName = dstDir + "/query_" + str(copy)
            with open(fileName, "w") as df:
                for l in storedLines:
                    df.write(l.lstrip().rstrip())    
                    df.write("\n") 
            break          
    return index

def splitFiles(startToken, endToken, fileName):
    f = open(fileName, "r")
    constructData = f.readlines()
    f.close()
    index = 0
    for i, line in enumerate(constructData):
        if index > 0:
            index -= 1
            continue 
        if startToken in line:
            print("Start token")
            index = findEndTokenIndexApi(constructData[i:], endToken) 
    
def cleanFile(fileName):
    try:
        with open(fileName, mode="r", encoding="utf-8") as sf:
            # Trim space tabs newline
            pass
    except FileNotFoundError as e:
        print("Error: File doesn't exist.")

# python split.py fileName startToken endToken
if ((not len(sys.argv) % 2 ) and len(sys.argv) > 4):
    print("Error: Wrong number of arguments.")
    exit()
elif (len(sys.argv) == 2):
    fileName = sys.argv[1]
elif (len(sys.argv) == 4):
    fileName = sys.argv[1]
    startToken = sys.argv[2]
    endToken = sys.argv[3]
else:
    dstDir = sys.argv[4]
print("Split files")
splitFiles(startToken, endToken, fileName)

# 
# ...
# parser = OptionParser()
# parser.add_option("-f", "--file", dest="filename",
#                   help="write report to FILE", metavar="FILE")
# parser.add_option("-q", "--quiet",
#                   action="store_false", dest="verbose", default=True,
#                   help="don't print status messages to stdout")

# (options, args) = parser.parse_args()
