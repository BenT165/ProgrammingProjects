import re
import sys
from socket import *
from os import *
from struct import *
from collections import namedtuple
import random

# by Ben Tate and Nina Vu

header = namedtuple("header", [
    'ID'
    , 'QR'
    , 'Opcode'
    , 'AA'
    , 'TC',
    'RD',
    'RA',
    'Z',
    'RCODE',
    'QDCOUNT',
    'ANCOUNT',
    'NSCOUNT',
    'ARCOUNT'
])

question = namedtuple("question", [
    'QNAME',
    'QTYPE',
    'QCLASS',
])

answer = namedtuple("answer", [
    'NAME',
    'Type',
    'Class',
    'TTL',
    'RDLENGTH',
    'RDATA',
])


# creates header for query (format slightly different for response)
def create_header():

    rand = random.randint(0, 32767) # generate random number for ID

    my_header = pack(">H", rand)  # second argument is ID

    # Flags (QR, opcode, AA, TC, RD, RA, Z, RCODE)
    # set Flag: Only set RD to 1 for recursive.
    flag = 0b0000000100000000

    my_header += pack(">H", flag)  # flag

    my_header += pack(">H", 1)  # QDCOUNT
    my_header += pack(">H", 0)  # ANCOUNT
    my_header += pack(">H", 0)  # NSCOUNT
    my_header += pack(">H", 0)  # ARCOUNT

    return my_header


# formats question for query
def create_question(domain_name):
    # initialize byte array to store question
    qname = bytearray()

    # remove any white space or new line characters from domain name
    domain_name = re.sub(r'\s+', '', domain_name)

    # separate by periods
    domain_name = domain_name.split(".")

    # encode the length of each letter sequence, followed by its letters
    for atom in domain_name:
        qname += pack(">B", len(atom))
        qname += atom.encode('ascii')

    # terminate byte sequence
    qname += pack(">B", 0)

    # QTYPE=A
    qtype = pack(">H", 1)

    # QCLASS=1 for internet
    qclass = pack(">H", 1)

    question = qname + qtype + qclass

    return question, len(qname)


# builds query message to send through socket
def create_query(domain_name):
    print("Preparing DNS query..")
    quest, len_qname = create_question(domain_name)
    dns_query = create_header() + quest
    return dns_query, len_qname


# send query to host through socket
def send_query(domain_name):
    serverName = '8.8.8.8'  # per project specs
    serverPort = 53  # standard port for DNS queries

    # TODO get user input for query message from command line
    message, qn_len = create_query(domain_name)

    print("Contacting DNS server..")
    clientSocket = socket(AF_INET, SOCK_DGRAM)  # open socket

    # set timeout for 5 seconds
    clientSocket.settimeout(5.0)

    try_num = 1
    no_issues = False

    print('Sending DNS query..')

    # try to send query up to 3 times
    while try_num <= 3:

        try:
            clientSocket.sendto(message, (serverName, serverPort))
            # clientSocket.sendto(message.encode(), (serverName, serverPort))
            print("Attempt " + str(try_num) + " out of 3 to send query..")
            modifiedMessage, serverAddress = clientSocket.recvfrom(2048)

            # executes if everything went as planned
            print("DNS response received (attempt " + str(try_num) + " out of 3)")
            no_issues = True
            break

        except socket.timeout:
            print("Attempt %d out of 3 failed due to timeout\n", try_num)

        finally:
            try_num += 1

    return no_issues, clientSocket, modifiedMessage, qn_len, serverAddress


# processes response to query
def process_response(query_sent, socket, response, question_length, serverAddress):
    print("Processing DNS response..")
    print("−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−")

    if query_sent:
        # decode and print header
        num_ans = 0
        re_header, num_records = decode_header(response)  # use in decode_answer (num resource records)
        re_question, answer_section_start = decode_question(response, question_length)
        while num_ans < num_records:
            answer_section_start = decode_answer(response, answer_section_start)  # figure out where last rr finished
            num_ans += 1

        # print(decode_host(modifiedMessage))
    else:
        print("DNS response not received\n")
    socket.close()


# display header info from DNS response
def decode_header(msg):

    # unpack six layer of the msg:
    raw_header = unpack_from(">HHHHHH", msg, 0)
    id = raw_header[0]
    flag_group = raw_header[1]
    qr = flag_group >> 15
    opcode = (flag_group & 0x7800) >> 11
    aa = (flag_group & 0x0400) >> 10
    tc = (flag_group & 0x0200) >> 9
    rd = (flag_group & 0x0100) >> 8
    ra = (flag_group & 0x0080) >> 7
    z = (flag_group & 0x0070) >> 4
    rcode = (flag_group & 0x000f)

    qdcount = raw_header[2]
    ancount = raw_header[3]
    nscount = raw_header[4]
    arcount = raw_header[5]

    # display header info as integers values
    print("header.ID = " + str(id))
    print("header.QR = " + str(qr))
    print("header.OPCODE = " + str(opcode))
    print("header.AA = " + f"{aa:0{1}b}")
    print("header.TC = " + f"{tc:0{1}b}")
    print("header.RD = " + f"{rd:0{1}b}")
    print("header.RA = " + f"{ra:0{1}b}")
    print("header.RCCODE = " + str(rcode))
    print("header.QDCOUNT = " + str(qdcount))
    print("header.ANCOUNT = " + str(ancount))
    print("header.NSCOUNT = " + str(nscount))
    print("header.ARCOUNT = " + str(arcount) + "\n")

    return header(id, qr, opcode, aa, tc, rd, ra, z, rcode, qdcount, ancount, nscount, arcount), ancount


def decode_question(msg, len_qname):
    header_length = 12
    qname_bytes = msg[header_length:header_length + len_qname]
    qtype_bytes = msg[header_length + len_qname: header_length + len_qname + 2]
    qclass_bytes = msg[header_length + len_qname + 2: header_length + len_qname + 4]

    # Convert qname_bytes to hex representation
    hex_list = []
    number = True
    length = 0
    index = 0

    # decode starting at index
    while qname_bytes[index] != 0:
        if number:
            length = qname_bytes[index]  # get length of sequence as integer
            index += 1
            number = False
        else:
            # decode character subsequence of <length> into hex and append to hex_list
            seq = qname_bytes[index: index + length]
            for b in seq:
                hex_list.append("{:02x}".format(b))
            index += length
            length = 0
            number = True

    de_qname = " ".join(hex_list)
    print("question.QNAME = " + de_qname)
    de_qtype = str(int.from_bytes(qtype_bytes, 'big'))
    print("question.QTYPE = " + de_qtype)
    de_qclass = str(int.from_bytes(qclass_bytes, 'big'))
    print("question.QCLASS = " + de_qclass + "\n")

    return question(de_qname, de_qtype, de_qclass), len(qname_bytes) + len(qtype_bytes) + len(
        qclass_bytes) + header_length


def decode_answer(msg, start):
    de_name, type_position = decode_answer_name(msg, start)
    de_type, class_position = decode_answer_type(msg, type_position)
    de_class, ttl_position = decode_answer_class(msg, class_position)
    de_ttl, rdl_position = decode_answer_ttl(msg, ttl_position)
    de_rdl, rda_position = decode_answer_rdl(msg, rdl_position)
    de_rdata, next_rr_index = decode_answer_rdata(msg, rda_position, de_rdl)
    return next_rr_index


def decode_answer_class(msg, class_position):
    de_class = int.from_bytes(msg[class_position: class_position + 2], byteorder='big')
    print("answer.CLASS: " + str(de_class))
    return de_class, class_position + 2


def decode_answer_type(msg, type_position):
    de_type = int.from_bytes(msg[type_position: type_position + 2], byteorder='big')
    print("answer.TYPE: " + str(de_type))
    return de_type, type_position + 2


def decode_answer_ttl(msg, ttl_position):
    de_ttl = int.from_bytes(msg[ttl_position: ttl_position + 4], byteorder='big')
    print("answer.TTL: " + str(de_ttl))
    return de_ttl, ttl_position + 4


def decode_answer_rdl(msg, rdl_position):
    de_rdl = int.from_bytes(msg[rdl_position: rdl_position + 2], byteorder='big')
    print("answer.RDL: " + str(de_rdl))
    return de_rdl, rdl_position + 2


def decode_answer_rdata(msg, rdata_position, rdlength):
    # dtype ==A:
    fmt_str = ">" + "B" * rdlength
    rdata = unpack_from(fmt_str, msg, rdata_position)

    ip = ''
    for byte in rdata:
        ip += str(byte) + '.'
    ip = ip[0:-1]
    print("answer.RDATA: " + str(ip) + "\n")
    next_rr_index = rdata_position+rdlength

    return ip, next_rr_index


def process_sequence(msg, start, domain_name, pointer_representation):
    index = start
    number = True
    length = 0

    domain_name.clear()  # Clear any previous labels

    while msg[index] != 0:
        if msg[index] & 0xC0 == 0xC0:  # byte at index is an address
            ptr = msg[index:index+2]
            index = ((0x3F & ptr[0]) << 8) | ptr[1]
        elif number:  # byte at index is the 3 number of characters in portion of domain name
            length = msg[index]
            index += 1
            number = False
        else: # byte at index is a character
            seq = msg[index: index + length]
            domain_name.append(seq.decode('ascii', 'strict'))
            index += length
            length = 0
            number = True

    de_name = '.'.join(domain_name)
    print('answer.NAME: ' + de_name)

    if pointer_representation:
        return de_name, start + 2
    else:
        return de_name, index + 1
    return de_name, return_index


def decode_answer_name(msg, start):
    domain_name = []
    pointer_representation = (msg[start] & 0xC0) == 0xC0
    de_name, next_index = process_sequence(msg, start, domain_name, pointer_representation)
    return de_name, next_index


def main():

    # Check the number of command-line arguments
    if len(sys.argv) < 2:
        print("Usage: python script.py <your_input>")
    else:
        # The first element (sys.argv[0]) is the script name, so the input is at sys.argv[1]
        user_input = sys.argv[1]
        print("$ my-dns-client: ", user_input)
        query_sent, socket, response, response_length, serverAddress = send_query(user_input)
        process_response(query_sent, socket, response, response_length, serverAddress)

if __name__ == '__main__':
    main()