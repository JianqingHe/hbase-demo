package hbasedemo.hbasedemo.core;

import lombok.Data;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页参数
 *
 * @author hejq
 * @date 2019/5/28 11:37
 */
@Data
public class PageInfo<T> implements Pageable {

    int value = 10;

    /**
     * 分页大小
     */
    int size = 10;

    /**
     * 页码
     */
    int page = 0;

    /**
     * 排序字段
     */
    String[] sorts = {};

    /**
     * 数据集合
     */
    List<T> data = new ArrayList<>();

    /**
     * 搜索关键字
     */
    String keyword;

    /**
     * 总数
     */
    long total = 0;

    /**
     * Returns the number of pages in the set.
     * To enable advanced printing features,
     * it is recommended that <code>Pageable</code>
     * implementations return the true number of pages
     * rather than the
     * UNKNOWN_NUMBER_OF_PAGES constant.
     *
     * @return the number of pages in this <code>Pageable</code>.
     */
    @Override
    public int getNumberOfPages() {
        return 0;
    }

    /**
     * Returns the <code>PageFormat</code> of the page specified by
     * <code>pageIndex</code>.
     *
     * @param pageIndex the zero based index of the page whose
     *                  <code>PageFormat</code> is being requested
     * @return the <code>PageFormat</code> describing the size and
     * orientation.
     * @throws IndexOutOfBoundsException if
     *                                   the <code>Pageable</code> does not contain the requested
     *                                   page.
     */
    @Override
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return null;
    }

    /**
     * Returns the <code>Printable</code> instance responsible for
     * rendering the page specified by <code>pageIndex</code>.
     *
     * @param pageIndex the zero based index of the page whose
     *                  <code>Printable</code> is being requested
     * @return the <code>Printable</code> that renders the page.
     * @throws IndexOutOfBoundsException if
     *                                   the <code>Pageable</code> does not contain the requested
     *                                   page.
     */
    @Override
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return null;
    }
}
