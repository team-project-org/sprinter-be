package hackathon.sprinter.util

import hackathon.sprinter.configure.DataNotFoundException
import hackathon.sprinter.configure.dto.ErrorCode
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import java.io.Reader

class CSVReader(
    private val reader: Reader
) {
    private val parser: CSVParser = CSVParser(reader, CSVFormat.DEFAULT)
    private val recodeList: MutableList<CSVRecord> = parser.records

    fun getRecodeList(): MutableList<CSVRecord> {
        return recodeList
    }

    fun readColumn(columnIdx: Int): List<String> {
        return recodeList.map { it[columnIdx] }
    }

    fun readColumn(columnHeader: String): List<String> {
        val columnIdx = recodeList[0].indexOf(columnHeader)
        return recodeList
            .slice(1 until recodeList.size)
            .map { it[columnIdx] }
    }

    fun <T> readMultiColumn(columnHeaderList: List<String>, ret: (List<String>) -> T): List<T> {
        val headerList = columnHeaderList.map { header ->
            recodeList[0].first { header in it }
        }

        val columnIdxList = headerList.map { recodeList[0].indexOf(it) }
        return try {
            recodeList.slice(1 until recodeList.size).map { ret(columnIdxList.map { idx -> it[idx] }) }
        } catch (idx: ArrayIndexOutOfBoundsException) {
            throw DataNotFoundException(
                ErrorCode.ITEM_NOT_EXIST,
                "해당하는 컬럼이 없습니다. $columnHeaderList: $columnIdxList"
            )
        }
    }
}