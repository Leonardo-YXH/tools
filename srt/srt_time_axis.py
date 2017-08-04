import sys, getopt, os


def modify_time_axis(src=None, dest=None, millisecond=0):
    if src is None:
        print("please select your srt file")
        return
    else:
        with open(src, 'r+') as f:
            new_content = []
            line_number = 0
            for line in f.readlines():
                if line_number % 4 == 1:
                    new_content.append(move_time(millisecond, line))
                else:
                    new_content.append(line)
                line_number += 1
    if dest is None:
        with open(src, "w+") as f:
            f.writelines(new_content)
    else:
        with open(dest, "w+") as f:
            f.writelines(new_content)


def move_time(millisecond, line):
    time_s = line.split("-->")
    if len(time_s) != 2:
        return line
    start_t = Y_Time(time_s[0].strip())
    start_t.change_time(millisecond)
    end_t = Y_Time(time_s[1].strip())
    end_t.change_time(millisecond)
    return " ".join([start_t.to_format_string(), "-->", end_t.to_format_string()]) + "\n"


class Y_Time(object):
    def __init__(self, format_str):
        """
        
        :param format_str: 00:00:00,000
        """
        tmp = format_str.split(",")
        self._millisecond = int(tmp[1])
        tmp = tmp[0].split(":")
        self._hour = int(tmp[0])
        self._minute = int(tmp[1])
        self._second = int(tmp[2])
        self._value = self._hour
        self._value = self._value * 60 + self._minute
        self._value = self._value * 60 + self._second
        self._value = self._value * 1000 + self._millisecond

    def change_time(self, millisecond):
        self._value += millisecond

    def parse_value(self):
        if self._value <= 0:
            self._hour = 0
            self._minute = 0
            self._second = 0
            self._millisecond = 0
        else:
            second = self._value // 1000
            self._millisecond = self._value - second * 1000
            minute = second // 60
            self._second = second - minute * 60
            hour = minute // 60
            self._minute = minute - hour * 60
            self._hour = hour

    def to_format_string(self):
        self.parse_value()
        return "{:0>2d}:{:0>2d}:{:0>2d},{:0>3d}".format(self._hour, self._minute, self._second, self._millisecond)


def main():
    ops, args = getopt(sys.argv[1:], "hi:o:f:t:")
    # for op,value in ops:
    if ops.has_key("-h"):
        print("""Usage
            -h details for options
            -i input file
            -o output file.if none,then will use input file
            -f input file fold,if this,all files in this fold will be as input file
            -t change time(millisecond),positive or negative
            """)
    else:
        if ops.has_key("-t"):
            millisecond = int(ops.get("-t", default=0))
            if ops.has_key("-f"):
                file_dir = ops.get("-f")
                for fn in os.listdir(file_dir):
                    modify_time_axis(fn, millisecond=millisecond)
            else:
                src = ops.get("-i")
                if ops.has_key("-o"):
                    dest = ops.get("-o")
                    modify_time_axis(src, dest, millisecond)
                else:
                    modify_time_axis(src, millisecond=millisecond)
        else:
            print("""unknow command
            to see more details please using <-h>
            """)


if __name__ == "__main__":
    main()
    # modify_time_axis("G://yangxh/videos/(English)Lecture 2 - Word Vector Representations- word2vec.srt",
    #                  millisecond=-2000)
