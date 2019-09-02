import pandas as pd
import win32com.client as win32
import numpy as np




# inf=['2018.8.17', '宁国路', '上海站', '地铁', '检查样机', None, None, None, None, 4.0]
# data=pd.DataFrame([['2018.8.17', '宁国路', '上海站', '地铁', '检查样机', None, None, None, None, 4.0]],index=[11],columns=list('BCDEFGHIJK'))
# data.loc[12]=inf
# print(data)
import sys
sys.path.append('../')
from usermodules import file_operate


def get_excel_dididatas(filespath,reason):
    """

    :param filespath: 文件路径数组
    :param reason:出差原因
    :return: 滴滴pdf数据整合为需要excel填写的数据
    """
    data=[]
    for filepath in filespath:
        tmp=append_data(filepath,reason)
        if not len(tmp):
            print(f'文件路径：{filepath}   无法获取报销信息')
            continue
        if not len(data):
            data=tmp
            continue
        data=np.vstack((data,tmp))
    return data


def append_data(filepath,reason):
    """
    获取每个pdf上的报销信息
    :param filespath: 文件路径
    :param reason: 出差原因
    :return: 如果没有返回空数组，如果有返回[[...]]
    """
    inf=[]
    with open(filepath,'rt',encoding='utf-8') as f:
        txt=f.read()
        f.close()
    if txt is None:
        return []

    srt=txt.find('快车')
    if srt<0:
        return []
    while True:
        if srt<0:
            break
        end=txt.find('\n',srt)
        if end<0:
            print(txt[srt:])
            break
        data=txt[srt:end].split(" ")
        if len(data)!=9:
            break
        data=[data[1],data[5],data[6],"出租车",reason,"","","","","","",data[8]]
        inf.append(data)
        srt=txt.find("快车",end)
    return inf
# ------------------------填写的信息--------------------------
REASON="青岛四方智能工作台调试"
filelist=file_operate.get_files_by_extension(r'E:\myproject\bxzdh\pdf识别','txt')
inf = get_excel_dididatas(filelist,REASON)
print(inf)
print(len(inf[0]))
#---------------------------excel操作---------------------------
SRT_ROW=11
END_ROW=11
excel=win32.gencache.EnsureDispatch('Excel.Application')
wb=excel.Workbooks.Open(r'E:\myproject\bxzdh\表格处理\模板.xlsx')
excel.Visible=True
ws=wb.Worksheets(1)
for data in inf:
    cell_range=f'B{SRT_ROW}:M{END_ROW}'
    print(cell_range)
    print(data)
    ws.Range(cell_range).Value=tuple(data)
    SRT_ROW+=1
    END_ROW+=1
import os
p=os.getcwd()
wb.SaveAs(p+r'\4.xlsx')
excel.Application.Quit()
