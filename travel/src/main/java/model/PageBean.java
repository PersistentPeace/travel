package model;

import java.util.ArrayList;
import java.util.List;

public class PageBean {

    //1_当前页中的数据集合
    private List data=new ArrayList();

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    //2_分页的参数信息
    //2_分页的参数信息
    private int curPage; //从客户端发送到服务端,已知的数据
    private int pageSize;//每页大小,提前约定好的,已知的数据,每页3条数据
    private int count;     //总记录数,需要通过select count(*) from contact查询

    private int startIndex; //limit后的第1个参数
    private int totalPage; //总共页数
    private int prevPage; //上1页
    private int nextPage; //下1页

    //抽取2个参数,起始页码,终止页码
    private int startPage;
    private int endPage;


    public PageBean(int curPage,int pageSize,int count){
        this.curPage=curPage;
        this.pageSize=pageSize;
        this.count=count;

        //计算limit后的第1个参数
        this.startIndex=(curPage-1)*pageSize;
        //总页数
        // 3 30 10
        // 3 32 11
        totalPage= (count%pageSize==0)?(count/pageSize):(count/pageSize+1);
        //上1页
        if(curPage!=1){
            prevPage=curPage-1;
        }else{
            prevPage=1;
        }
        //下1页
        if(curPage!=totalPage){
            nextPage=curPage+1;
        }else{
            nextPage=totalPage;
        }

        //计算起始和结束页码
        startPage = curPage - 4;
        endPage = curPage + 4;
        //看看总页数够不够9页
        if(totalPage>9){
            //超过了9页
            if(startPage < 1){
                startPage = 1;
                endPage = startPage+8;
            }
            if(endPage>totalPage){
                endPage = totalPage;
                startPage = endPage-8;
            }
        }else{
            //不够9页
            startPage = 1;
            endPage = totalPage;
        }
        System.out.println(endPage);

    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
}
