@Grab(group = 'io.github.http-builder-ng', module = 'http-builder-ng-okhttp', version = '1.0.3')
@Grab(group = 'org.slf4j', module = 'slf4j-nop', version = '1.7.25')

import groovyx.net.http.HttpBuilder
import groovyx.net.http.ContentTypes

def http = HttpBuilder.configure {
    request.uri = 'http://localhost:8080/batch/school'
    request.contentType = ContentTypes.JSON[0]
    request.charset = 'UTF-8'
}

readBatch('school.csv', 40) { chunk ->
    println http.post {
        request.body = chunk
    }
}

static void readBatch(String filename, int chunkSize, Closure callable) {
    def chunk = []

    def toMap = { fields ->
        [
            number: fields[0],
            name: fields[1],
            type: fields[2],
            city: fields[3],
            region: fields[4],
            country: fields[5]
        ]
    }

    new File(filename).eachLine { String line, Integer count ->

        if(count == 1) {
            def names = line.split('\t')

            toMap = { fields ->
                def map = [:]
                for (int i = 0; i < fields.size(); i++) {
                    map[names[i]] = fields[i]
                }
                map
            }
            return
        }

        def fields = line.split('\t')
        chunk << toMap(fields)

        if(count % chunkSize == 0) {
            callable(chunk)
            chunk = []
        }
    }

    if (chunk) {
        callable(chunk)
    }
}