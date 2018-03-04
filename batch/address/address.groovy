@Grab(group = 'io.github.http-builder-ng', module = 'http-builder-ng-okhttp', version = '1.0.3')
@Grab(group = 'org.slf4j', module = 'slf4j-nop', version = '1.7.25')

import groovyx.net.http.HttpBuilder
import groovyx.net.http.ContentTypes


def http = HttpBuilder.configure {
    request.uri = 'http://localhost:8080/batch/address'
    request.contentType = ContentTypes.JSON[0]
    request.charset = 'UTF-8'
}

readBatch('address.csv', 40) { chunk ->
    http.post {
        request.body = chunk
    }
}

static void readBatch(String filename, int chunkSize, Closure callable) {
    def chunk = []

    new File(filename).eachLine { String line, Integer count ->
        def fields = line.split(';')
        chunk << [
                city: fields[0],
                region: fields[1],
                country: fields[2]
        ]
        if(count % chunkSize == 0) {
            callable(chunk)
            chunk = []
        }
    }

    if (chunk) {
        callable(chunk)
    }
}
