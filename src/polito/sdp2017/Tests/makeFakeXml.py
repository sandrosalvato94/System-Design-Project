#!/usr/bin/env python

if __name__ == "__main__":
    from xml.etree.ElementTree import Element, SubElement, Comment, tostring
    from xml.etree import ElementTree
    from xml.dom import minidom

    import os
    import os.path
    import random

    def xmlToString(elem):
        rough_string = ElementTree.tostring(elem, 'utf-8')
        reparsed = minidom.parseString(rough_string)
        return reparsed.toprettyxml(indent="    ")

    def generateFakeContactPoint(top,name):
        ContactPoint = SubElement(top,'ContactPoint')
        AuthorName = SubElement(ContactPoint,'AuthorName')
        AuthorName.text = name
        AuthorId = SubElement(ContactPoint,'AuthorId')
        AuthorId.text = 'id of '+name
        AuthorCompany = SubElement(ContactPoint,'AuthorCompany')
        AuthorCompany.text = 'company of '+name
        AuthorEmail = SubElement(ContactPoint,'AuthorEmail')
        AuthorEmail.text = 'email of '+name
        AuthorRole = SubElement(ContactPoint,'AuthorRole')
        AuthorRole.text = 'role of '+name

    def generateFakeHwProp(top):
        HardwareProperties = SubElement(top,'HardwareProperties')
        HardwarePropertiesLUTs = SubElement(HardwareProperties,'HardwarePropertiesLUTs')
        HardwarePropertiesLUTs.text = str(random.randint(0,1000))
        HardwarePropertiesFFs = SubElement(HardwareProperties,'HardwarePropertiesFFs')
        HardwarePropertiesFFs.text = str(random.randint(0,1000))
        HardwarePropertiesLatency = SubElement(HardwareProperties,'HardwarePropertiesLatency')
        HardwarePropertiesLatency.text = str(random.uniform(0,10))[0:5]
        HardwarePropertiesNMemories = SubElement(HardwareProperties,'HardwarePropertiesNMemories')
        HardwarePropertiesNMemories.text = str(random.randint(0,10000))
        HardwarePropertiesPowerConsumption = SubElement(HardwareProperties,'HardwarePropertiesPowerConsumption')
        HardwarePropertiesPowerConsumption.text = str(random.uniform(0,10))[0:5]
        HardwarePropertiesMaxClkFreq = SubElement(HardwareProperties,'HardwarePropertiesMaxClkFreq')
        HardwarePropertiesMaxClkFreq.text = str(random.uniform(0,10))[0:5]

    def generateFakeXml(path,name):
        print('generating XML for : '+name)
        top = Element('IPs')
        core = SubElement(top,'IPCore')
        IPName = SubElement(core,'IPName')
        IPName.text = name
        IPDescription = SubElement(core,'IPDescription')
        IPDescription.text = 'A description for '+name

        if random.random() < 0.5:
            cPoint = "EmanueleParisi"
        else:
            cPoint = "AlessandroSalvato"

        generateFakeContactPoint(core,cPoint)
        generateFakeHwProp(core)

        IPHdlSourcePath = SubElement(core,'IPHdlSourcePath')
        IPHdlSourcePath.text = 'src/polito/sdp2017/Tests/'+path
        IPDriverPath = SubElement(core,'IPDriverPath')
        IPDriverPath.text = '/path/to/driver/'+name

        fp = open(name+'.xml','w')
        fp.write(xmlToString(top))
        fp.close()

#### MAIN ####
    print("Fake script for generating fake IP XML files...");
    current_path = os.getcwd();

    for f in os.listdir(current_path):
        fname, fext = os.path.splitext(f)
        if fext == ".vhd":
            generateFakeXml(f,fname)
