# encoding=utf-8
import jieba

def 参值变换(word):
    谦辞=['我','我的','在下','敝人','贱人','鄙人','小的','俺','喃','咱','吾人','吾']
    敬辞=['你','您','你们','您们','你的','汝','尔']
    if word in 谦辞 or 敬辞:
        if word in 谦辞:
            return True
        if word in 敬辞:
            return False
    else:
        return 0

def replaceWords(string1,sex,identity,Generation):
    # 1读取同义词表，并生成一个字典。
    谦辞词典 = {}
    if Generation != 2:
        for line in open(r"D:\TiTextProcessing\FastAPI\venv\words2.txt", "r", encoding='utf-8'):
            seperate_word = line.strip().split(" ")
            num = len(seperate_word)
            for i in range(1, num):
                谦辞词典[seperate_word[i]] = seperate_word[0]
    # print(谦辞词典)
    if identity== 1:
        for line in open(r"D:\TiTextProcessing\FastAPI\venv\BroSis.txt", "r", encoding='utf-8'):
            seperate_word = line.strip().split(" ")
            num = len(seperate_word)
            for i in range(1, num):
                谦辞词典[seperate_word[i]] = seperate_word[0]
    if identity== 2:
        for line in open(r"D:\TiTextProcessing\FastAPI\venv\parent.txt", "r", encoding='utf-8'):
            seperate_word = line.strip().split(" ")
            num = len(seperate_word)
            for i in range(1, num):
                谦辞词典[seperate_word[i]] = seperate_word[0]
    敬辞词典 = {}
    # synonymWords.txt是同义词表，每行是一系列同义词，用" "分割
    if Generation != 2:
        for line in open(r"D:\TiTextProcessing\FastAPI\venv\words.txt", "r", encoding='utf-8'):
            seperate_word = line.strip().split(" ")
            num = len(seperate_word)
            for i in range(1, num):
                敬辞词典[seperate_word[i]] = seperate_word[0]
    # print(敬辞词典)
    if sex==False:
        for line in open(r'D:\TiTextProcessing\FastAPI\venv\girlWord.txt',"r", encoding='utf-8'):
            seperate_word=line.strip().split(" ")
            num = len(seperate_word)
            for i in range(1,num):
                敬辞词典[seperate_word[i]]=seperate_word[0]
    if identity== 2:
        for line in open(r"D:\TiTextProcessing\FastAPI\venv\parent.txt", "r", encoding='utf-8'):
            seperate_word = line.strip().split(" ")
            num = len(seperate_word)
            for i in range(1, num):
                敬辞词典[seperate_word[i]] = seperate_word[0]
    # print(敬辞词典)
    # 3将语句切分成单词
    jieba.load_userdict('D:/TiTextProcessing/FastAPI/venv/my_dict.txt')
    seg_list = jieba.cut(string1, cut_all=False)
    f = "/".join(seg_list).encode("utf-8")
    f = f.decode("utf-8")
    # print(f)
    # 4返回同义词替换后的句子
    final_sentence = " "
    control=True
    for word in f.split('/'):
        if 参值变换(word)==True:
            control=True
        if 参值变换(word)==False:
            control=False
        if control==False:
            if word in 敬辞词典:
                word = 敬辞词典[word]
                final_sentence += word
            else:
                final_sentence += word
        else:
            if word in 谦辞词典:
                word = 谦辞词典[word]
                final_sentence += word
            else:
                final_sentence += word
    print(final_sentence)
    return final_sentence