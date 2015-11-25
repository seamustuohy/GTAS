(function () {
    'use strict';
    app.controller('FlightsController', function ($scope, $state, $interval, $stateParams, passengersBasedOnUserFilter,flightService, gridService, uiGridConstants, executeQueryService, flights, flightsModel) {
        $scope.model = flightsModel;

        var self = this, contacts;
        /* returns id of airport object function for a query string */
        function returnObjectId (o) { return o.id; }
        /* Create filter function for a query string */
        function createFilterFor(query) {
            var lowercaseQuery = query.toLowerCase();
            return function filterFn(contact) {
                return (contact.lowerCasedName.indexOf(lowercaseQuery) >= 0);
            };
        }
        /* Search for contacts. */
        function querySearch(query) {
            return query && query.length ? self.allContacts.filter(createFilterFor(query)) : [];
        }
        self.querySearch = querySearch;
        contacts = [
            {
                "id": "TBJ",
                "name": "7 Novembre (TBJ)"
            },
            {
                "id": "LCG",
                "name": "A Coruna (LCG)"
            },
            {
                "id": "AAL",
                "name": "Aalborg (AAL)"
            },
            {
                "id": "AAR",
                "name": "Aarhus (AAR)"
            },
            {
                "id": "JEG",
                "name": "Aasiaat (JEG)"
            },
            {"id": "ABD", "name": "Abadan (ABD)"},
            {"id": "ABF", "name": "Abaiang Atoll Airport (ABF)"},
            {"id": "ABA", "name": "Abakan (ABA)"},
            {"id": "YXX", "name": "Abbotsford (YXX)"},
            {"id": "MLG", "name": "Abdul Rachman Saleh (MLG)"},
            {"id": "AEH", "name": "Abeche (AEH)"},
            {"id": "SNU", "name": "Abel Santamaria (SNU)"},
            {"id": "AEA", "name": "Abemama Atoll Airport (AEA)"},
            {"id": "ABR", "name": "Aberdeen Regional Airport (ABR)"},
            {"id": "AHB", "name": "Abha (AHB)"},
            {"id": "ABJ", "name": "Abidjan Felix Houphouet Boigny Intl (ABJ)"},
            {"id": "ABI", "name": "Abilene Rgnl (ABI)"},
            {"id": "CJS", "name": "Abraham Gonzalez Intl (CJS)"},
            {"id": "SPI", "name": "Abraham Lincoln Capital (SPI)"},
            {"id": "AUH", "name": "Abu Dhabi Intl (AUH)"},
            {"id": "ABS", "name": "Abu Simbel (ABS)"},
            {"id": "ARA", "name": "Acadiana Rgnl (ARA)"},
            {"id": "SRG", "name": "Achmad Yani (SRG)"},
            {"id": "ACU", "name": "Achutupo Airport (ACU)"},
            {"id": "ADK", "name": "Adak Airport (ADK)"},
            {"id": "LIT", "name": "Adams Fld (LIT)"},
            {"id": "ADA", "name": "Adana (ADA)"},
            {"id": "UAB", "name": "Adana-Incirlik Airbase (UAB)"},
            {"id": "ADS", "name": "Addison (ADS)"},
            {"id": "ADL", "name": "Adelaide Intl (ADL)"},
            {"id": "MGQ", "name": "Aden Adde International Airport (MGQ)"},
            {"id": "ADE", "name": "Aden Intl (ADE)"},
            {"id": "SOC", "name": "Adi Sumarmo Wiryokusumo (SOC)"},
            {"id": "JOG", "name": "Adi Sutjipto (JOG)"},
            {"id": "SLK", "name": "Adirondack Regional Airport (SLK)"},
            {"id": "ADF", "name": "Adiyaman Airport (ADF)"},
            {"id": "ADB", "name": "Adnan Menderes (ADB)"},
            {"id": "CIO", "name": "Aeroclub Cioca (CIO)"},
            {"id": "DZM", "name": "Aeroclub Cluj (DZM)"},
            {"id": "DVA", "name": "Aeroclub Deva (DVA)"},
            {"id": "SIB", "name": "Aeroclub Sibiu (SIB)"},
            {"id": "QNV", "name": "Aeroclube de Nova Iguacu (QNV)"},
            {"id": "AEP", "name": "Aeroparque Jorge Newbery (AEP)"},
            {"id": "BNU", "name": "Aeroporto Blumenau (BNU)"},
            {"id": "BPS", "name": "Aeroporto de Porto Seguro (BPS)"},
            {"id": "BJP", "name": "Aeroporto Estadual Arthur Siqueira (BJP)"},
            {"id": "JDR", "name": "Aeroporto Prefeito Octavio de Almeida Neves (JDR)"},
            {"id": "WPR", "name": "Aeropuerto Capitan Fuentes Martinez (WPR)"},
            {"id": "RAF", "name": "Aeropuerto de Rafaela (RAF)"},
            {"id": "RLO", "name": "Aeropuerto Internacional Valle del Conlara (RLO)"},
            {"id": "TTQ", "name": "Aerotortuguero Airport (TTQ)"},
            {"id": "CWB", "name": "Afonso Pena (CWB)"},
            {"id": "AFT", "name": "Afutara Airport (AFT)"},
            {"id": "AFY", "name": "Afyon (AFY)"},
            {"id": "IXA", "name": "Agartala (IXA)"},
            {"id": "AGX", "name": "Agatti (AGX)"},
            {"id": "AGZ", "name": "Aggeneys (AGZ)"},
            {"id": "AGR", "name": "Agra (AGR)"},
            {"id": "AJI", "name": "Agri Airport (AJI)"},
            {"id": "AGQ", "name": "Agrinion (AGQ)"},
            {"id": "OCV", "name": "Aguas Claras (OCV)"},
            {"id": "AGJ", "name": "Aguni Airport (AGJ)"},
            {"id": "AHE", "name": "Ahe Airport (AHE)"},
            {"id": "AMD", "name": "Ahmedabad (AMD)"},
            {"id": "AHS", "name": "Ahuas Airport (AHS)"},
            {"id": "AWZ", "name": "Ahwaz (AWZ)"},
            {"id": "AIM", "name": "Ailuk Airport (AIM)"},
            {"id": "QSF", "name": "Ain Arnat Airport (QSF)"},
            {"id": "AIE", "name": "Aiome (AIE)"},
            {"id": "IEO", "name": "Aioun El Atrouss (IEO)"},
            {"id": "ZIN", "name": "Air Base (ZIN)"},
            {"id": "AIC", "name": "Airok Airport (AIC)"},
            {"id": "VDS", "name": "Airport (VDS)"},
            {"id": "AIT", "name": "Aitutaki (AIT)"},
            {"id": "CMF", "name": "Aix Les Bains (CMF)"},
            {"id": "QXB", "name": "Aix Les Milles (QXB)"},
            {"id": "AJL", "name": "Aizawl (AJL)"},
            {"id": "AKK", "name": "Akhiok Airport (AKK)"},
            {"id": "AKI", "name": "Akiak Airport (AKI)"},
            {"id": "AXT", "name": "Akita (AXT)"},
            {"id": "LAK", "name": "Aklavik (LAK)"},
            {"id": "AKD", "name": "Akola (AKD)"},
            {"id": "CAK", "name": "Akron Canton Regional Airport (CAK)"},
            {"id": "AKC", "name": "Akron Fulton Intl (AKC)"},
            {"id": "AKT", "name": "Akrotiri (AKT)"},
            {"id": "AKU", "name": "Aksu Airport (AKU)"},
            {"id": "SCO", "name": "Aktau (SCO)"},
            {"id": "PVK", "name": "Aktio (PVK)"},
            {"id": "AKX", "name": "Aktyubinsk (AKX)"},
            {"id": "AKV", "name": "Akulivik Airport (AKV)"},
            {"id": "QCU", "name": "Akunnaaq Heliport (QCU)"},
            {"id": "AKR", "name": "Akure (AKR)"},
            {"id": "AEY", "name": "Akureyri (AEY)"},
            {"id": "KQA", "name": "Akutan Seaplane Base (KQA)"},
            {"id": "HOF", "name": "Al Ahsa (HOF)"},
            {"id": "AAN", "name": "Al Ain International Airport (AAN)"},
            {"id": "ABT", "name": "Al Baha (ABT)"},
            {"id": "AAY", "name": "Al Ghaidah Intl (AAY)"},
            {"id": "NJF", "name": "Al Najaf International Airport (NJF)"},
            {"id": "IUD", "name": "Al Udeid AB (IUD)"},
            {"id": "AJF", "name": "Al-Jawf Domestic Airport (AJF)"},
            {"id": "AUK", "name": "Alakanuk Airport (AUK)"},
            {"id": "DBB", "name": "Alalamain Intl. (DBB)"},
            {"id": "ALM", "name": "Alamogordo White Sands Regional Airport (ALM)"},
            {"id": "ALH", "name": "Albany Airport (ALH)"},
            {"id": "ALB", "name": "Albany Intl (ALB)"},
            {"id": "ALL", "name": "Albenga (ALL)"},
            {"id": "OAJ", "name": "Albert J Ellis (OAJ)"},
            {"id": "SPG", "name": "Albert Whitted (SPG)"},
            {"id": "MRD", "name": "Alberto Carnevalli (MRD)"},
            {"id": "TND", "name": "Alberto Delgado Airport (TND)"},
            {"id": "JHL", "name": "Albian Aerodrome (JHL)"},
            {"id": "ABN", "name": "Albina Airstrip (ABN)"},
            {"id": "ABQ", "name": "Albuquerque International Sunport (ABQ)"},
            {"id": "ABX", "name": "Albury (ABX)"},
            {"id": "ACD", "name": "Alcides Fernandez Airport (ACD)"},
            {"id": "ADH", "name": "Aldan Airport (ADH)"},
            {"id": "ACI", "name": "Alderney (ACI)"},
            {"id": "WKK", "name": "Aleknagik Airport (WKK)"},
            {"id": "ALP", "name": "Aleppo Intl (ALP)"},
            {"id": "YLT", "name": "Alert (YLT)"},
            {"id": "ALJ", "name": "Alexander Bay (ALJ)"},
            {"id": "ISW", "name": "Alexander Field South Wood County Airport (ISW)"},
            {"id": "ALR", "name": "Alexandra (ALR)"},
            {"id": "ALX", "name": "Alexandria (ALX)"},
            {"id": "AEX", "name": "Alexandria Intl (AEX)"},
            {"id": "ALY", "name": "Alexandria Intl (ALY)"},
            {"id": "JSI", "name": "Alexandros Papadiamantis (JSI)"},
            {"id": "PKH", "name": "Alexion (PKH)"},
            {"id": "HUU", "name": "Alferez Fap David Figueroa Fernandini Airport (HUU)"},
            {"id": "CLO", "name": "Alfonso Bonilla Aragon Intl (CLO)"},
            {"id": "VUP", "name": "Alfonso Lopez Pumarejo (VUP)"},
            {"id": "LET", "name": "Alfredo Vasquez Cobo (LET)"},
            {"id": "AHO", "name": "Alghero (AHO)"},
            {"id": "ALC", "name": "Alicante (ALC)"},
            {"id": "ALI", "name": "Alice Intl (ALI)"},
            {"id": "ASP", "name": "Alice Springs (ASP)"},
            {"id": "ALZ", "name": "Alitak Seaplane Base (ALZ)"},
            {"id": "BJS", "name": "All Airports (BJS)"},
            {"id": "CHI", "name": "All Airports (CHI)"},
            {"id": "LON", "name": "All Airports (LON)"},
            {"id": "MIL", "name": "All Airports (MIL)"},
            {"id": "NYC", "name": "All Airports (NYC)"},
            {"id": "PAR", "name": "All Airports (PAR)"},
            {"id": "TYO", "name": "All Airports (TYO)"},
            {"id": "WAS", "name": "All Airports (WAS)"},
            {"id": "YMQ", "name": "All Airports (YMQ)"},
            {"id": "YTO", "name": "All Airports (YTO)"},
            {"id": "IXD", "name": "Allahabad (IXD)"},
            {"id": "AET", "name": "Allakaket Airport (AET)"},
            {"id": "LHE", "name": "Allama Iqbal Intl (LHE)"},
            {"id": "ADY", "name": "Alldays Airport (ADY)"},
            {"id": "AGC", "name": "Allegheny County Airport (AGC)"},
            {"id": "BIG", "name": "Allen Aaf (BIG)"},
            {"id": "FMM", "name": "Allgau (FMM)"},
            {"id": "AIA", "name": "Alliance Municipal Airport (AIA)"},
            {"id": "LLU", "name": "Alluitsup Paa Heliport (LLU)"},
            {"id": "YTF", "name": "Alma Airport (YTF)"},
            {"id": "ALA", "name": "Almaty (ALA)"},
            {"id": "LEI", "name": "Almeria (LEI)"},
            {"id": "RCH", "name": "Almirante Padilla (RCH)"},
            {"id": "REL", "name": "Almirante Zar (REL)"},
            {"id": "CTD", "name": "Alonso Valderrama Airport (CTD)"},
            {"id": "APN", "name": "Alpena County Regional Airport (APN)"},
            {"id": "ALF", "name": "Alta (ALF)"},
            {"id": "AFL", "name": "Alta Floresta (AFL)"},
            {"id": "LTI", "name": "Altai Airport (LTI)"},
            {"id": "ATM", "name": "Altamira (ATM)"},
            {"id": "AAT", "name": "Altay Airport (AAT)"},
            {"id": "AOC", "name": "Altenburg Nobitz (AOC)"},
            {"id": "ARR", "name": "Alto Rio Senguer Airport (ARR)"},
            {"id": "AOO", "name": "Altoona Blair Co (AOO)"},
            {"id": "LTS", "name": "Altus Afb (LTS)"},
            {"id": "ALU", "name": "Alula Airport (ALU)"},
            {"id": "NSK", "name": "Alykel (NSK)"},
            {"id": "AMC", "name": "Am Timan Airport (AMC)"},
            {"id": "AXJ", "name": "Amakusa Airfield (AXJ)"},
            {"id": "ASJ", "name": "Amami (ASJ)"},
            {"id": "CPQ", "name": "Amarais Airport (CPQ)"},
            {"id": "ERN", "name": "Amaury Feitosa Tomaz Airport (ERN)"},
            {"id": "WAI", "name": "Ambalabe (WAI)"},
            {"id": "IVA", "name": "Ambanja Airport (IVA)"},
            {"id": "AMY", "name": "Ambatomainty Airport (AMY)"},
            {"id": "WAM", "name": "Ambatondrazaka Airport (WAM)"},
            {"id": "AMB", "name": "Ambilobe (AMB)"},
            {"id": "ABL", "name": "Ambler Airport (ABL)"},
            {"id": "MZI", "name": "Ambodedjo (MZI)"},
            {"id": "ASV", "name": "Amboseli Airport (ASV)"},
            {"id": "JIB", "name": "Ambouli International Airport (JIB)"},
            {"id": "COR", "name": "Ambrosio L V Taravella (COR)"},
            {"id": "AUJ", "name": "Ambunti (AUJ)"},
            {"id": "AMV", "name": "Amderma Airport (AMV)"},
            {"id": "XZK", "name": "Amherst Amtrak Station AMM (XZK)"},
            {"id": "SID", "name": "Amilcar Cabral Intl (SID)"},
            {"id": "AOS", "name": "Amook Bay Seaplane Base (AOS)"},
            {"id": "GOY", "name": "Amparai (GOY)"},
            {"id": "SAY", "name": "Ampugnano (SAY)"},
            {"id": "ATQ", "name": "Amritsar (ATQ)"},
            {"id": "ZYA", "name": "Amsterdam Centraal (ZYA)"},
            {"id": "ZYA", "name": "Amsterdam Centraal (ZYA)"},
            {"id": "AAA", "name": "Anaa (AAA)"},
            {"id": "AAO", "name": "Anaco (AAO)"},
            {"id": "OTS", "name": "Anacortes Airport (OTS)"},
            {"id": "AOE", "name": "Anadolu Airport (AOE)"},
            {"id": "YAA", "name": "Anahim Lake Airport (YAA)"},
            {"id": "AKP", "name": "Anaktuvuk Pass Airport (AKP)"},
            {"id": "HVA", "name": "Analalava (HVA)"},
            {"id": "SDN", "name": "Anda Airport (SDN)"},
            {"id": "ANS", "name": "Andahuaylas (ANS)"},
            {"id": "ZWA", "name": "Andapa (ZWA)"},
            {"id": "DVD", "name": "Andavadoaka (DVD)"},
            {"id": "ANX", "name": "Andenes (ANX)"},
            {"id": "UAM", "name": "Andersen Afb (UAM)"},
            {"id": "AND", "name": "Anderson Rgnl (AND)"},
            {"id": "AZN", "name": "Andizhan Airport (AZN)"},
            {"id": "AAP", "name": "Andrau Airport (AAP)"},
            {"id": "PYR", "name": "Andravida (PYR)"},
            {"id": "ADW", "name": "Andrews Afb (ADW)"},
            {"id": "AUY", "name": "Anelghowhat Airport (AUY)"},
            {"id": "OUD", "name": "Angads (OUD)"},
            {"id": "TGZ", "name": "Angel Albino Corzo (TGZ)"},
            {"id": "LFK", "name": "Angelina Co (LFK)"},
            {"id": "QXG", "name": "Angers St Laud (QXG)"},
            {"id": "ANE", "name": "Angers-Loire Airport (ANE)"},
            {"id": "HLY", "name": "Anglesey Airport (HLY)"},
            {"id": "BIQ", "name": "Anglet (BIQ)"},
            {"id": "AGN", "name": "Angoon Seaplane Base (AGN)"},
            {"id": "ANI", "name": "Aniak Airport (ANI)"},
            {"id": "AWD", "name": "Aniwa Airport (AWD)"},
            {"id": "AKA", "name": "Ankang Airport (AKA)"},
            {"id": "JVA", "name": "Ankavandra Airport (JVA)"},
            {"id": "IKV", "name": "Ankeny Regl Airport (IKV)"},
            {"id": "ARB", "name": "Ann Arbor Municipal Airport (ARB)"},
            {"id": "AAE", "name": "Annaba (AAE)"},
            {"id": "NAI", "name": "Annai Airport (NAI)"},
            {"id": "QNJ", "name": "Annemasse (QNJ)"},
            {"id": "ANN", "name": "Annette Island (ANN)"},
            {"id": "ANB", "name": "Anniston Metro (ANB)"},
            {"id": "AQG", "name": "Anqing Airport (AQG)"},
            {"id": "AYT", "name": "Antalya (AYT)"},
            {"id": "SJI", "name": "Antique (SJI)"},
            {"id": "OES", "name": "Antoine De St Exupery Airport (OES)"},
            {"id": "CUM", "name": "Antonio Jose De Sucre (CUM)"},
            {"id": "ARE", "name": "Antonio Juarbe Pol Airport (ARE)"},
            {"id": "SCU", "name": "Antonio Maceo Intl (SCU)"},
            {"id": "PSO", "name": "Antonio Narino (PSO)"},
            {"id": "WAQ", "name": "Antsalova Airport (WAQ)"},
            {"id": "ANM", "name": "Antsirabato (ANM)"},
            {"id": "ATJ", "name": "Antsirabe (ATJ)"},
            {"id": "ANV", "name": "Anvik Airport (ANV)"},
            {"id": "AOJ", "name": "Aomori (AOJ)"},
            {"id": "AOT", "name": "Aosta Airport (AOT)"},
            {"id": "AAF", "name": "Apalachicola Regional Airport (AAF)"},
            {"id": "APO", "name": "Apartadó Airport (APO)"},
            {"id": "APK", "name": "Apataki Airport (APK)"},
            {"id": "X04", "name": "Apopka (X04)"},
            {"id": "ATW", "name": "Appleton (ATW)"},
            {"id": "AQJ", "name": "Aqaba King Hussein Intl (AQJ)"},
            {"id": "ARU", "name": "Aracatuba (ARU)"},
            {"id": "ARW", "name": "Arad (ARW)"},
            {"id": "AUX", "name": "Araguaina Airport (AUX)"},
            {"id": "RKP", "name": "Aransas County Airport (RKP)"},
            {"id": "AAK", "name": "Aranuka Airport (AAK)"},
            {"id": "RAE", "name": "Arar (RAE)"},
            {"id": "ACR", "name": "Araracuara Airport (ACR)"},
            {"id": "AQA", "name": "Araraquara (AQA)"},
            {"id": "AAX", "name": "Araxa Airport (AAX)"},
            {"id": "GPA", "name": "Araxos (GPA)"},
            {"id": "AMH", "name": "Arba Minch (AMH)"},
            {"id": "ACV", "name": "Arcata (ACV)"},
            {"id": "YAB", "name": "Arctic Bay Airport (YAB)"},
            {"id": "ARC", "name": "Arctic Village Airport (ARC)"},
            {"id": "ADU", "name": "Ardabil Airport (ADU)"},
            {"id": "OBS", "name": "Ardeche Meridionale (OBS)"},
            {"id": "AMZ", "name": "Ardmore (AMZ)"},
            {"id": "ADM", "name": "Ardmore Muni (ADM)"},
            {"id": "FON", "name": "Arenal Airport (FON)"},
            {"id": "GYL", "name": "Argyle Airport (GYL)"},
            {"id": "KSO", "name": "Aristotelis (KSO)"},
            {"id": "AYK", "name": "Arkalyk Airport (AYK)"},
            {"id": "BYH", "name": "Arkansas Intl (BYH)"},
            {"id": "ARN", "name": "Arlanda (ARN)"},
            {"id": "GKY", "name": "Arlington Municipal (GKY)"},
            {"id": "ARM", "name": "Armidale (ARM)"},
            {"id": "SBK", "name": "Armor (SBK)"},
            {"id": "YYW", "name": "Armstrong (YYW)"},
            {"id": "LME", "name": "Arnage (LME)"},
            {"id": "LBE", "name": "Arnold Palmer Regional Airport (LBE)"},
            {"id": "ZCA", "name": "Arnsberg Menden (ZCA)"},
            {"id": "MOL", "name": "Aro (MOL)"},
            {"id": "AIS", "name": "Arorae Island Airport (AIS)"},
            {"id": "DIE", "name": "Arrachart (DIE)"},
            {"id": "EPS", "name": "Arroyo Barril Intl (EPS)"},
            {"id": "X21", "name": "Arthur Dunn Airpark (X21)"},
            {"id": "ATC", "name": "Arthurs Town Airport (ATC)"},
            {"id": "SCL", "name": "Arturo Merino Benitez Intl (SCL)"},
            {"id": "VLN", "name": "Arturo Michelena Intl (VLN)"},
            {"id": "RUA", "name": "Arua Airport (RUA)"},
            {"id": "ARK", "name": "Arusha (ARK)"},
            {"id": "AXR", "name": "Arutua (AXR)"},
            {"id": "AVK", "name": "Arvaikheer Airport (AVK)"},
            {"id": "YEK", "name": "Arviat (YEK)"},
            {"id": "AJR", "name": "Arvidsjaur (AJR)"},
            {"id": "AKJ", "name": "Asahikawa (AKJ)"},
            {"id": "AAU", "name": "Asau Airport (AAU)"},
            {"id": "AVL", "name": "Asheville Regional Airport (AVL)"},
            {"id": "LYM", "name": "Ashford (LYM)"},
            {"id": "ASB", "name": "Ashgabat (ASB)"},
            {"id": "3G4", "name": "Ashland County Airport (3G4)"},
            {"id": "ASM", "name": "Asmara Intl (ASM)"},
            {"id": "ASO", "name": "Asosa (ASO)"},
            {"id": "ASE", "name": "Aspen Pitkin County Sardy Field (ASE)"},
            {"id": "ASA", "name": "Assab Intl (ASA)"},
            {"id": "TSE", "name": "Astana Intl (TSE)"},
            {"id": "AST", "name": "Astoria Regional Airport (AST)"},
            {"id": "ASF", "name": "Astrakhan (ASF)"},
            {"id": "OVD", "name": "Asturias (OVD)"},
            {"id": "JTY", "name": "Astypalaia (JTY)"},
            {"id": "ASW", "name": "Aswan Intl (ASW)"},
            {"id": "ATZ", "name": "Asyut International Airport (ATZ)"},
            {"id": "AXK", "name": "Ataq (AXK)"},
            {"id": "ATR", "name": "Atar (ATR)"},
            {"id": "IST", "name": "Ataturk (IST)"},
            {"id": "ATB", "name": "Atbara Airport (ATB)"},
            {"id": "HEW", "name": "Athen Helenikon Airport (HEW)"},
            {"id": "AHN", "name": "Athens Ben Epps Airport (AHN)"},
            {"id": "YIB", "name": "Atikokan Muni (YIB)"},
            {"id": "AIU", "name": "Atiu Island Airport (AIU)"},
            {"id": "AKB", "name": "Atka Airport (AKB)"},
            {"id": "FFC", "name": "Atlanta Regional Airport - Falcon Field (FFC)"},
            {
                "id": "ACY",
                "name": "Atlantic City Intl (ACY)"
            },
            {
                "id": "ZRA",
                "name": "Atlantic City Rail Terminal (ZRA)"
            },
            {
                "id": "369",
                "name": "Atmautluak Airport (369)"
            },
            {
                "id": "ATK",
                "name": "Atqasuk Edward Burnell Sr Memorial Airport (ATK)"
            },
            {
                "id": "YAT",
                "name": "Attawapiskat Airport (YAT)"
            },
            {
                "id": "GUW",
                "name": "Atyrau (GUW)"
            },
            {
                "id": "AUO",
                "name": "Auburn University Regional (AUO)"
            },
            {
                "id": "AKL",
                "name": "Auckland Intl (AKL)"
            },
            {
                "id": "AGB",
                "name": "Augsburg (AGB)"
            },
            {
                "id": "AUB",
                "name": "Augsburg HBF (AUB)"
            },
            {
                "id": "ZAU",
                "name": "Augsburg Railway (ZAU)"
            },
            {
                "id": "AGS",
                "name": "Augusta Rgnl At Bush Fld (AGS)"
            },
            {
                "id": "AUG",
                "name": "Augusta State (AUG)"
            },
            {
                "id": "NAT",
                "name": "Augusto Severo (NAT)"
            },
            {
                "id": "AKS",
                "name": "Auki Airport (AKS)"
            },
            {
                "id": "YPJ",
                "name": "Aupaluk Airport (YPJ)"
            },
            {
                "id": "AUL",
                "name": "Aur Island Airport (AUL)"
            },
            {
                "id": "IXU",
                "name": "Aurangabad (IXU)"
            },
            {
                "id": "BBU",
                "name": "Aurel Vlaicu (BBU)"
            },
            {
                "id": "AUR",
                "name": "Aurillac (AUR)"
            },
            {
                "id": "AUU",
                "name": "Aurukun Airport (AUU)"
            },
            {
                "id": "AUS",
                "name": "Austin Bergstrom Intl (AUS)"
            },
            {
                "id": "GRB",
                "name": "Austin Straubel Intl (GRB)"
            },
            {
                "id": "CFE",
                "name": "Auvergne (CFE)"
            },
            {
                "id": "AVV",
                "name": "Avalon (AVV)"
            },
            {
                "id": "AVX",
                "name": "Avalon (AVX)"
            },
            {
                "id": "WMR",
                "name": "Avaratra (WMR)"
            },
            {
                "id": "CPC",
                "name": "Aviador C Campos (CPC)"
            },
            {
                "id": "AVB",
                "name": "Aviano Ab (AVB)"
            },
            {
                "id": "AWB",
                "name": "Awaba Airport (AWB)"
            },
            {
                "id": "AWA",
                "name": "Awasa Airport (AWA)"
            },
            {
                "id": "AXU",
                "name": "Axum (AXU)"
            },
            {
                "id": "AYQ",
                "name": "Ayers Rock (AYQ)"
            },
            {
                "id": "MBU",
                "name": "Babanakira Airport (MBU)"
            },
            {
                "id": "ROR",
                "name": "Babelthuap (ROR)"
            },
            {
                "id": "IEG",
                "name": "Babimost (IEG)"
            },
            {
                "id": "BXB",
                "name": "Babo (BXB)"
            },
            {
                "id": "BFH",
                "name": "Bacacheri (BFH)"
            },
            {
                "id": "BCM",
                "name": "Bacau (BCM)"
            },
            {
                "id": "BCO",
                "name": "Baco Airport (BCO)"
            },
            {
                "id": "BCD",
                "name": "Bacolod (BCD)"
            },
            {
                "id": "FKB",
                "name": "Baden Airpark (FKB)"
            },
            {
                "id": "BDD",
                "name": "Badu Island Airport (BDD)"
            },
            {
                "id": "BFX",
                "name": "Bafoussam (BFX)"
            },
            {
                "id": "BPE",
                "name": "Bagan (BPE)"
            },
            {
                "id": "NYU",
                "name": "Bagan Intl (NYU)"
            },
            {
                "id": "E51",
                "name": "Bagdad Airport (E51)"
            },
            {
                "id": "IXB",
                "name": "Bagdogra (IXB)"
            },
            {
                "id": "BGW",
                "name": "Baghdad International Airport (BGW)"
            },
            {
                "id": "YBG",
                "name": "Bagotville (YBG)"
            },
            {
                "id": "BPM",
                "name": "Bagram AFB (BPM)"
            },
            {
                "id": "BAG",
                "name": "Baguio (BAG)"
            },
            {
                "id": "BHV",
                "name": "Bahawalpur Airport (BHV)"
            },
            {
                "id": "BFQ",
                "name": "Bahia Piña Airport (BFQ)"
            },
            {
                "id": "HUX",
                "name": "Bahias De Huatulco Intl (HUX)"
            },
            {
                "id": "BJR",
                "name": "Bahir Dar (BJR)"
            },
            {
                "id": "BAH",
                "name": "Bahrain Intl (BAH)"
            },
            {
                "id": "YBC",
                "name": "Baie Comeau (YBC)"
            },
            {
                "id": "LZH",
                "name": "Bailian Airport (LZH)"
            },
            {
                "id": "BBR",
                "name": "Baillif Airport (BBR)"
            },
            {
                "id": "VMU",
                "name": "Baimuru Airport (VMU)"
            },
            {
                "id": "BSJ",
                "name": "Bairnsdale Airport (BSJ)"
            },
            {
                "id": "HET",
                "name": "Baita Airport (HET)"
            },
            {
                "id": "CAN",
                "name": "Baiyun Intl (CAN)"
            },
            {
                "id": "BJH",
                "name": "Bajhang (BJH)"
            },
            {
                "id": "BJU",
                "name": "Bajura Airport (BJU)"
            },
            {
                "id": "BKM",
                "name": "Bakalalan Airport (BKM)"
            },
            {
                "id": "BXE",
                "name": "Bakel (BXE)"
            },
            {
                "id": "YBK",
                "name": "Baker Lake (YBK)"
            },
            {
                "id": "BWO",
                "name": "Balakovo Airport (BWO)"
            },
            {
                "id": "CEK",
                "name": "Balandino (CEK)"
            },
            {
                "id": "MLJ",
                "name": "Baldwin County Airport (MLJ)"
            },
            {
                "id": "MLH",
                "name": "Bale Mulhouse (MLH)"
            },
            {
                "id": "DPS",
                "name": "Bali Ngurah Rai (DPS)"
            },
            {
                "id": "KRK",
                "name": "Balice (KRK)"
            },
            {
                "id": "BZI",
                "name": "Balikesir (BZI)"
            },
            {
                "id": "EDO",
                "name": "Balikesir Korfez Airport (EDO)"
            },
            {
                "id": "OPU",
                "name": "Balimo Airport (OPU)"
            },
            {
                "id": "BKN",
                "name": "Balkanabat (BKN)"
            },
            {
                "id": "BXH",
                "name": "Balkhash Airport (BXH)"
            },
            {
                "id": "BAS",
                "name": "Ballalae Airport (BAS)"
            },
            {
                "id": "BNK",
                "name": "Ballina Byron Gateway (BNK)"
            },
            {
                "id": "BBA",
                "name": "Balmaceda (BBA)"
            },
            {
                "id": "BZY",
                "name": "Balti International Airport (BZY)"
            },
            {
                "id": "BWI",
                "name": "Baltimore Washington Intl (BWI)"
            },
            {
                "id": "BXR",
                "name": "Bam Airport (BXR)"
            },
            {
                "id": "ABM",
                "name": "Bamaga Injinoo (ABM)"
            },
            {
                "id": "BAM",
                "name": "Bamberg BF (BAM)"
            },
            {
                "id": "99N",
                "name": "Bamberg County Airport (99N)"
            },
            {
                "id": "BMQ",
                "name": "Bamburi (BMQ)"
            },
            {
                "id": "BPC",
                "name": "Bamenda (BPC)"
            },
            {
                "id": "BIN",
                "name": "Bamyan Airport (BIN)"
            },
            {
                "id": "OUI",
                "name": "Ban Huoeisay Airport (OUI)"
            },
            {
                "id": "LKL",
                "name": "Banak (LKL)"
            },
            {
                "id": "NDA",
                "name": "Bandanaira Airport (NDA)"
            },
            {
                "id": "BND",
                "name": "Bandar Abbass Intl (BND)"
            },
            {
                "id": "BDH",
                "name": "Bandar Lengeh (BDH)"
            },
            {
                "id": "CMB",
                "name": "Bandaranaike Intl Colombo (CMB)"
            },
            {
                "id": "NWA",
                "name": "Bandaressalam (NWA)"
            },
            {
                "id": "BDM",
                "name": "Bandirma (BDM)"
            },
            {
                "id": "FDU",
                "name": "Bandundu (FDU)"
            },
            {
                "id": "BLR",
                "name": "Bangalore (BLR)"
            },
            {
                "id": "BGR",
                "name": "Bangor Intl (BGR)"
            },
            {
                "id": "BGF",
                "name": "Bangui M Poko (BGF)"
            },
            {
                "id": "BNX",
                "name": "Banja Luka International Airport (BNX)"
            },
            {
                "id": "BJL",
                "name": "Banjul Intl (BJL)"
            },
            {
                "id": "ME5",
                "name": "Banks Airport (ME5)"
            },
            {
                "id": "BMO",
                "name": "Banmaw Airport (BMO)"
            },
            {
                "id": "BNP",
                "name": "Bannu Airport (BNP)"
            },
            {
                "id": "BYT",
                "name": "Bantry Aerodrome (BYT)"
            },
            {
                "id": "SZX",
                "name": "Baoan Intl (SZX)"
            },
            {
                "id": "BSD",
                "name": "Baoshan Airport (BSD)"
            },
            {
                "id": "BAV",
                "name": "Baotou Airport (BAV)"
            },
            {
                "id": "DLL",
                "name": "Baraboo Wisconsin Dells Airport (DLL)"
            },
            {
                "id": "MGN",
                "name": "Baracoa (MGN)"
            },
            {
                "id": "MAD",
                "name": "Barajas (MAD)"
            },
            {
                "id": "ULV",
                "name": "Barataevka (ULV)"
            },
            {
                "id": "BEJ",
                "name": "Barau(Kalimaru) Airport (BEJ)"
            },
            {
                "id": "QYR",
                "name": "Barberey (QYR)"
            },
            {
                "id": "BCI",
                "name": "Barcaldine Airport (BCI)"
            },
            {
                "id": "BCN",
                "name": "Barcelona (BCN)"
            },
            {
                "id": "BAZ",
                "name": "Barcelos Airport (BAZ)"
            },
            {
                "id": "BDU",
                "name": "Bardufoss (BDU)"
            },
            {
                "id": "BRI",
                "name": "Bari (BRI)"
            },
            {
                "id": "BNS",
                "name": "Barinas (BNS)"
            },
            {
                "id": "BBN",
                "name": "Bario Airport (BBN)"
            },
            {
                "id": "BZL",
                "name": "Barisal Airport (BZL)"
            },
            {
                "id": "BKH",
                "name": "Barking Sands Pmrf (BKH)"
            },
            {
                "id": "PAH",
                "name": "Barkley Regional Airport (PAH)"
            },
            {
                "id": "BAD",
                "name": "Barksdale Afb (BAD)"
            },
            {
                "id": "BAX",
                "name": "Barnaul (BAX)"
            },
            {
                "id": "BAF",
                "name": "Barnes Municipal (BAF)"
            },
            {
                "id": "HYA",
                "name": "Barnstable Muni Boardman Polando Fld (HYA)"
            },
            {
                "id": "BRM",
                "name": "Barquisimeto Intl (BRM)"
            },
            {
                "id": "BRR",
                "name": "Barra Airport (BRR)"
            },
            {
                "id": "BCL",
                "name": "Barra del Colorado Airport (BCL)"
            },
            {
                "id": "BRA",
                "name": "Barreiras Airport (BRA)"
            },
            {
                "id": "WDR",
                "name": "Barrow County Airport (WDR)"
            },
            {
                "id": "BWB",
                "name": "Barrow Island Airport (BWB)"
            },
            {
                "id": "BTI",
                "name": "Barter Island Lrrs (BTI)"
            },
            {
                "id": "BBH",
                "name": "Barth (BBH)"
            },
            {
                "id": "CAW",
                "name": "Bartolomeu Lisandro (CAW)"
            },
            {
                "id": "BOW",
                "name": "Bartow Municipal Airport (BOW)"
            },
            {
                "id": "9A5",
                "name": "Barwick Lafayette Airport (9A5)"
            },
            {
                "id": "HLJ",
                "name": "Barysiai (HLJ)"
            },
            {
                "id": "KRZ",
                "name": "Basango Mboliasa Airport (KRZ)"
            },
            {
                "id": "BSU",
                "name": "Basankusu Airport (BSU)"
            },
            {
                "id": "BSO",
                "name": "Basco Airport (BSO)"
            },
            {
                "id": "SSZ",
                "name": "Base Aerea De Santos (SSZ)"
            },
            {
                "id": "BSR",
                "name": "Basrah Intl (BSR)"
            },
            {
                "id": "MEK",
                "name": "Bassatine (MEK)"
            },
            {
                "id": "LTK",
                "name": "Bassel Al Assad Intl (LTK)"
            },
            {
                "id": "PGX",
                "name": "Bassillac (PGX)"
            },
            {
                "id": "BSG",
                "name": "Bata (BSG)"
            },
            {
                "id": "AZI",
                "name": "Bateen (AZI)"
            },
            {
                "id": "BHS",
                "name": "Bathurst Airport (BHS)"
            },
            {
                "id": "ZBF",
                "name": "Bathurst Airport (ZBF)"
            },
            {
                "id": "BRT",
                "name": "Bathurst Island Airport (BRT)"
            },
            {
                "id": "BAL",
                "name": "Batman (BAL)"
            },
            {
                "id": "BLJ",
                "name": "Batna Airport (BLJ)"
            },
            {
                "id": "BTR",
                "name": "Baton Rouge Metro Ryan Fld (BTR)"
            },
            {
                "id": "BJF",
                "name": "Batsfjord (BJF)"
            },
            {
                "id": "BBM",
                "name": "Battambang Airport (BBM)"
            },
            {
                "id": "BUS",
                "name": "Batumi (BUS)"
            },
            {
                "id": "BPF",
                "name": "Batuna Airport (BPF)"
            },
            {
                "id": "BDE",
                "name": "Baudette Intl (BDE)"
            },
            {
                "id": "BAU",
                "name": "Bauru (BAU)"
            },
            {
                "id": "JTC",
                "name": "Bauru-Arealva (JTC)"
            },
            {
                "id": "BBJ",
                "name": "Bautzen (BBJ)"
            },
            {
                "id": "BYN",
                "name": "Bayankhongor Airport (BYN)"
            },
            {
                "id": "RLK",
                "name": "Bayannur (RLK)"
            },
            {
                "id": "BYU",
                "name": "Bayreuth (BYU)"
            },
            {
                "id": "BZB",
                "name": "Bazaruto Island Airport (BZB)"
            },
            {
                "id": "BAB",
                "name": "Beale Afb (BAB)"
            },
            {
                "id": "XBE",
                "name": "Bearskin Lake Airport (XBE)"
            },
            {
                "id": "BFT",
                "name": "Beaufort (BFT)"
            },
            {
                "id": "BMT",
                "name": "Beaumont Municipal (BMT)"
            },
            {
                "id": "DRI",
                "name": "Beauregard Rgnl (DRI)"
            },
            {
                "id": "WBQ",
                "name": "Beaver Airport (WBQ)"
            },
            {
                "id": "BFP",
                "name": "Beaver Falls (BFP)"
            },
            {
                "id": "BEU",
                "name": "Bedourie Airport (BEU)"
            },
            {
                "id": "BEC",
                "name": "Beech Factory Airport (BEC)"
            },
            {
                "id": "NBC",
                "name": "Begishevo (NBC)"
            },
            {
                "id": "BEI",
                "name": "Beica Airport (BEI)"
            },
            {
                "id": "BHY",
                "name": "Beihai Airport (BHY)"
            },
            {
                "id": "BHN",
                "name": "Beihan (BHN)"
            },
            {
                "id": "NAY",
                "name": "Beijing Nanyuan Airport (NAY)"
            },
            {
                "id": "BEW",
                "name": "Beira (BEW)"
            },
            {
                "id": "OVA",
                "name": "Bekily (OVA)"
            },
            {
                "id": "BLG",
                "name": "Belaga Airport (BLG)"
            },
            {
                "id": "UKS",
                "name": "Belbek Sevastopol International Airport (UKS)"
            },
            {
                "id": "BMY",
                "name": "Belep Islands Airport (BMY)"
            },
            {
                "id": "BHD",
                "name": "Belfast City (BHD)"
            },
            {
                "id": "BFS",
                "name": "Belfast Intl (BFS)"
            },
            {
                "id": "IXG",
                "name": "Belgaum (IXG)"
            },
            {
                "id": "EGO",
                "name": "Belgorod International Airport (EGO)"
            },
            {
                "id": "TZA",
                "name": "Belize City Municipal Airport (TZA)"
            },
            {
                "id": "ZEL",
                "name": "Bella Bella Airport (ZEL)"
            },
            {
                "id": "QBC",
                "name": "Bella Coola Airport (QBC)"
            },
            {
                "id": "BEP",
                "name": "Bellary (BEP)"
            },
            {
                "id": "LIG",
                "name": "Bellegarde (LIG)"
            },
            {
                "id": "BLI",
                "name": "Bellingham Intl (BLI)"
            },
            {
                "id": "BN1",
                "name": "Bellona (BN1)"
            },
            {
                "id": "BNY",
                "name": "Bellona (BNY)"
            },
            {
                "id": "BEO",
                "name": "Belmont Airport (BEO)"
            },
            {
                "id": "BMD",
                "name": "Belo sur Tsiribihina Airport (BMD)"
            },
            {
                "id": "EYK",
                "name": "Beloyarsky (EYK)"
            },
            {
                "id": "BBP",
                "name": "Bembridge (BBP)"
            },
            {
                "id": "BJI",
                "name": "Bemidji Regional Airport (BJI)"
            },
            {
                "id": "TLV",
                "name": "Ben Gurion (TLV)"
            },
            {
                "id": "BEB",
                "name": "Benbecula (BEB)"
            },
            {
                "id": "BUG",
                "name": "Benguela (BUG)"
            },
            {
                "id": "BCW",
                "name": "Benguera Island Airport (BCW)"
            },
            {
                "id": "BNI",
                "name": "Benin (BNI)"
            },
            {
                "id": "BEN",
                "name": "Benina (BEN)"
            },
            {
                "id": "NVA",
                "name": "Benito Salas (NVA)"
            },
            {
                "id": "BJT",
                "name": "Bentota Airport (BJT)"
            },
            {
                "id": "BEG",
                "name": "Beograd (BEG)"
            },
            {
                "id": "BPU",
                "name": "Beppu Airport (BPU)"
            },
            {
                "id": "BBO",
                "name": "Berbera (BBO)"
            },
            {
                "id": "BBT",
                "name": "Berberati (BBT)"
            },
            {
                "id": "YBV",
                "name": "Berens River (YBV)"
            },
            {
                "id": "NBB",
                "name": "Berezovo (NBB)"
            },
            {
                "id": "BGY",
                "name": "Bergamo Orio Al Serio (BGY)"
            },
            {
                "id": "BVG",
                "name": "Berlevag (BVG)"
            },
            {
                "id": "BER",
                "name": "Berlin Brandenburg Willy Brandt (BER)"
            },
            {
                "id": "GWW",
                "name": "Berlin Gatow (GWW)"
            },
            {
                "id": "QPP",
                "name": "Berlin Hauptbahnhof (QPP)"
            },
            {
                "id": "BJO",
                "name": "Bermejo (BJO)"
            },
            {
                "id": "UDD",
                "name": "Bermuda Dunes Airport (UDD)"
            },
            {
                "id": "BDA",
                "name": "Bermuda Intl (BDA)"
            },
            {
                "id": "BRN",
                "name": "Bern Belp (BRN)"
            },
            {
                "id": "BTM",
                "name": "Bert Mooney Airport (BTM)"
            },
            {
                "id": "BEZ",
                "name": "Beru Island Airport (BEZ)"
            },
            {
                "id": "BPY",
                "name": "Besalampy (BPY)"
            },
            {
                "id": "OGZ",
                "name": "Beslan Airport (OGZ)"
            },
            {
                "id": "EKY",
                "name": "Bessemer (EKY)"
            },
            {
                "id": "BET",
                "name": "Bethel (BET)"
            },
            {
                "id": "BTT",
                "name": "Bettles (BTT)"
            },
            {
                "id": "BVY",
                "name": "Beverly Municipal Airport (BVY)"
            },
            {
                "id": "BFT",
                "name": "BFT County Airport (BFT)"
            },
            {
                "id": "BWA",
                "name": "Bhairahawa (BWA)"
            },
            {
                "id": "BHR",
                "name": "Bharatpur Airport (BHR)"
            },
            {
                "id": "BHU",
                "name": "Bhavnagar (BHU)"
            },
            {
                "id": "BIY",
                "name": "Bhisho (BIY)"
            },
            {
                "id": "BHP",
                "name": "Bhojpur (BHP)"
            },
            {
                "id": "BHO",
                "name": "Bhopal (BHO)"
            },
            {
                "id": "BBI",
                "name": "Bhubaneshwar (BBI)"
            },
            {
                "id": "BHJ",
                "name": "Bhuj (BHJ)"
            },
            {
                "id": "PIS",
                "name": "Biard (PIS)"
            },
            {
                "id": "BYS",
                "name": "Bicycle Lake Aaf (BYS)"
            },
            {
                "id": "BIE",
                "name": "Biessenhofen BF (BIE)"
            },
            {
                "id": "YRR",
                "name": "Big Bay Water Aerodrome (YRR)"
            },
            {
                "id": "L35",
                "name": "Big Bear City (L35)"
            },
            {
                "id": "BGK",
                "name": "Big Creek Airport (BGK)"
            },
            {
                "id": "BMX",
                "name": "Big Mountain Afs (BMX)"
            },
            {
                "id": "6S0",
                "name": "Big Timber Airport (6S0)"
            },
            {
                "id": "YTL",
                "name": "Big Trout Lake Airport (YTL)"
            },
            {
                "id": "BQH",
                "name": "Biggin Hill (BQH)"
            },
            {
                "id": "BIF",
                "name": "Biggs Aaf (BIF)"
            },
            {
                "id": "BFJ",
                "name": "Bijie Feixiong Airport (BFJ)"
            },
            {
                "id": "PAB",
                "name": "Bilaspur (PAB)"
            },
            {
                "id": "BIO",
                "name": "Bilbao (BIO)"
            },
            {
                "id": "BIU",
                "name": "Bildudalur Airport (BIU)"
            },
            {
                "id": "BIL",
                "name": "Billings Logan International Airport (BIL)"
            },
            {
                "id": "BLL",
                "name": "Billund (BLL)"
            },
            {
                "id": "NSB",
                "name": "Bimini North Seaplane Base (NSB)"
            },
            {
                "id": "GNS",
                "name": "Binaka (GNS)"
            },
            {
                "id": "BGG",
                "name": "Bingol (BGG)"
            },
            {
                "id": "TSN",
                "name": "Binhai (TSN)"
            },
            {
                "id": "BTU",
                "name": "Bintulu (BTU)"
            },
            {
                "id": "BIR",
                "name": "Biratnagar (BIR)"
            },
            {
                "id": "KBC",
                "name": "Birch Creek Airport (KBC)"
            },
            {
                "id": "BDI",
                "name": "Bird Island Airport (BDI)"
            },
            {
                "id": "BVI",
                "name": "Birdsville Airport (BVI)"
            },
            {
                "id": "XBJ",
                "name": "Birjand (XBJ)"
            },
            {
                "id": "BHX",
                "name": "Birmingham (BHX)"
            },
            {
                "id": "BHM",
                "name": "Birmingham Intl (BHM)"
            },
            {
                "id": "IXR",
                "name": "Birsa Munda (IXR)"
            },
            {
                "id": "DUG",
                "name": "Bisbee Douglas Intl (DUG)"
            },
            {
                "id": "BHH",
                "name": "Bisha (BHH)"
            },
            {
                "id": "FNT",
                "name": "Bishop International (FNT)"
            },
            {
                "id": "BSK",
                "name": "Biskra (BSK)"
            },
            {
                "id": "BIS",
                "name": "Bismarck Municipal Airport (BIS)"
            },
            {
                "id": "BMM",
                "name": "Bitam (BMM)"
            },
            {
                "id": "SPF",
                "name": "Black Hills Airport-Clyde Ice Field (SPF)"
            },
            {
                "id": "ZUN",
                "name": "Black Rock (ZUN)"
            },
            {
                "id": "YBI",
                "name": "Black Tickle Airport (YBI)"
            },
            {
                "id": "BKQ",
                "name": "Blackall (BKQ)"
            },
            {
                "id": "BBS",
                "name": "Blackbushe (BBS)"
            },
            {
                "id": "BLK",
                "name": "Blackpool (BLK)"
            },
            {
                "id": "BLT",
                "name": "Blackwater Airport (BLT)"
            },
            {
                "id": "TLS",
                "name": "Blagnac (TLS)"
            },
            {
                "id": "BYW",
                "name": "Blakely Island Airport (BYW)"
            },
            {
                "id": "BID",
                "name": "Block Island State Airport (BID)"
            },
            {
                "id": "BFN",
                "name": "Bloemfontein Intl (BFN)"
            },
            {
                "id": "Y72",
                "name": "Bloyer Field (Y72)"
            },
            {
                "id": "LEX",
                "name": "Blue Grass (LEX)"
            },
            {
                "id": "BEF",
                "name": "Bluefields (BEF)"
            },
            {
                "id": "BLH",
                "name": "Blythe Airport (BLH)"
            },
            {
                "id": "KBS",
                "name": "Bo Airport (KBS)"
            },
            {
                "id": "BVB",
                "name": "Boa Vista (BVB)"
            },
            {
                "id": "IAN",
                "name": "Bob Baker Memorial Airport (IAN)"
            },
            {
                "id": "BUR",
                "name": "Bob Hope (BUR)"
            },
            {
                "id": "YBO",
                "name": "Bob Quinn Lake (YBO)"
            },
            {
                "id": "CEW",
                "name": "Bob Sikes (CEW)"
            },
            {
                "id": "BOY",
                "name": "Bobo Dioulasso (BOY)"
            },
            {
                "id": "BCT",
                "name": "Boca Raton (BCT)"
            },
            {
                "id": "BOC",
                "name": "Bocas Del Toro Intl (BOC)"
            },
            {
                "id": "BOX",
                "name": "Bochum HBF (BOX)"
            },
            {
                "id": "EBO",
                "name": "Bochum Railway (EBO)"
            },
            {
                "id": "ODO",
                "name": "Bodaibo (ODO)"
            },
            {
                "id": "BOO",
                "name": "Bodo (BOO)"
            },
            {
                "id": "BJV",
                "name": "Bodrum - Milas (BJV)"
            },
            {
                "id": "BFI",
                "name": "Boeing Fld King Co Intl (BFI)"
            },
            {
                "id": "XBG",
                "name": "Bogande Airport (XBG)"
            },
            {
                "id": "GIC",
                "name": "Boigu Airport (GIC)"
            },
            {
                "id": "ASH",
                "name": "Boire Field Airport (ASH)"
            },
            {
                "id": "BOI",
                "name": "Boise Air Terminal (BOI)"
            },
            {
                "id": "BJB",
                "name": "Bojnourd Airport (BJB)"
            },
            {
                "id": "BUI",
                "name": "Bokondini Airport (BUI)"
            },
            {
                "id": "ADD",
                "name": "Bole Intl (ADD)"
            },
            {
                "id": "BLQ",
                "name": "Bologna (BLQ)"
            },
            {
                "id": "PEE",
                "name": "Bolshoye Savino (PEE)"
            },
            {
                "id": "TZR",
                "name": "Bolton Field (TZR)"
            },
            {
                "id": "BZO",
                "name": "Bolzano (BZO)"
            },
            {
                "id": "LVR",
                "name": "Bom Futuro Airport (LVR)"
            },
            {
                "id": "LAZ",
                "name": "Bom Jesus Da Lapa (LAZ)"
            },
            {
                "id": "BOA",
                "name": "Boma Airport (BOA)"
            },
            {
                "id": "BZA",
                "name": "Bonanza Airport (BZA)"
            },
            {
                "id": "YVB",
                "name": "Bonaventure Airport (YVB)"
            },
            {
                "id": "BYO",
                "name": "Bonito Airport (BYO)"
            },
            {
                "id": "YBY",
                "name": "Bonnyville Airport (YBY)"
            },
            {
                "id": "TRW",
                "name": "Bonriki Intl (TRW)"
            },
            {
                "id": "HRO",
                "name": "Boone Co (HRO)"
            },
            {
                "id": "BXX",
                "name": "Boorama Airport (BXX)"
            },
            {
                "id": "BOB",
                "name": "Bora Bora (BOB)"
            },
            {
                "id": "RBB",
                "name": "Borba Airport (RBB)"
            },
            {
                "id": "BMW",
                "name": "Bordj Badji Mokhtar Airport (BMW)"
            },
            {
                "id": "HBE",
                "name": "Borg El Arab Intl (HBE)"
            },
            {
                "id": "BMK",
                "name": "Borkum (BMK)"
            },
            {
                "id": "BLE",
                "name": "Borlange (BLE)"
            },
            {
                "id": "RNN",
                "name": "Bornholm Ronne (RNN)"
            },
            {
                "id": "BPR",
                "name": "Borongan Airport (BPR)"
            },
            {
                "id": "BXS",
                "name": "Borrego Valley Airport (BXS)"
            },
            {
                "id": "KBP",
                "name": "Boryspil Intl (KBP)"
            },
            {
                "id": "BSA",
                "name": "Bosaso Airport (BSA)"
            },
            {
                "id": "OCJ",
                "name": "Boscobel (OCJ)"
            },
            {
                "id": "BOT",
                "name": "Bosset Airport (BOT)"
            },
            {
                "id": "BST",
                "name": "Bost Airport (BST)"
            },
            {
                "id": "ZTY",
                "name": "Boston Back Bay Station (ZTY)"
            },
            {
                "id": "BTO",
                "name": "Botopassi Airstrip (BTO)"
            },
            {
                "id": "QCJ",
                "name": "Botucatu (QCJ)"
            },
            {
                "id": "TID",
                "name": "Bou Chekif (TID)"
            },
            {
                "id": "BYK",
                "name": "Bouake (BYK)"
            },
            {
                "id": "QFD",
                "name": "Boufarik (QFD)"
            },
            {
                "id": "BLD",
                "name": "Boulder City Municipal Airport (BLD)"
            },
            {
                "id": "WBU",
                "name": "Boulder Municipal (WBU)"
            },
            {
                "id": "BQL",
                "name": "Boulia Airport (BQL)"
            },
            {
                "id": "YDT",
                "name": "Boundary Bay Airport (YDT)"
            },
            {
                "id": "BOU",
                "name": "Bourges (BOU)"
            },
            {
                "id": "BRK",
                "name": "Bourke Airport (BRK)"
            },
            {
                "id": "BOH",
                "name": "Bournemouth (BOH)"
            },
            {
                "id": "EBU",
                "name": "Boutheon (EBU)"
            },
            {
                "id": "HQM",
                "name": "Bowerman Field (HQM)"
            },
            {
                "id": "BWG",
                "name": "Bowling Green-Warren County Regional Airport (BWG)"
            },
            {
                "id": "LOU",
                "name": "Bowman Fld (LOU)"
            },
            {
                "id": "BWK",
                "name": "Brac (BWK)"
            },
            {
                "id": "41N",
                "name": "Braceville Airport (41N)"
            },
            {
                "id": "POC",
                "name": "Brackett Field (POC)"
            },
            {
                "id": "BFD",
                "name": "Bradford Regional Airport (BFD)"
            },
            {
                "id": "BDL",
                "name": "Bradley Intl (BDL)"
            },
            {
                "id": "BSF",
                "name": "Bradshaw Aaf (BSF)"
            },
            {
                "id": "BGC",
                "name": "Braganca (BGC)"
            },
            {
                "id": "BRD",
                "name": "Brainerd Lakes Rgnl (BRD)"
            },
            {
                "id": "BMP",
                "name": "Brampton Island (BMP)"
            },
            {
                "id": "OEB",
                "name": "Branch County Memorial Airport (OEB)"
            },
            {
                "id": "AUF",
                "name": "Branches (AUF)"
            },
            {
                "id": "YBR",
                "name": "Brandon Muni (YBR)"
            },
            {
                "id": "OQN",
                "name": "Brandywine Airport (OQN)"
            },
            {
                "id": "BKG",
                "name": "Branson LLC (BKG)"
            },
            {
                "id": "YFD",
                "name": "Brantford (YFD)"
            },
            {
                "id": "BTK",
                "name": "Bratsk (BTK)"
            },
            {
                "id": "BWE",
                "name": "Braunschweig Wolfsburg (BWE)"
            },
            {
                "id": "BRV",
                "name": "Bremerhaven (BRV)"
            },
            {
                "id": "PWT",
                "name": "Bremerton National (PWT)"
            },
            {
                "id": "MEN",
                "name": "Brenoux (MEN)"
            },
            {
                "id": "BQT",
                "name": "Brest (BQT)"
            },
            {
                "id": "BVS",
                "name": "Breves Airport (BVS)"
            },
            {
                "id": "KTS",
                "name": "Brevig Mission Airport (KTS)"
            },
            {
                "id": "ORE",
                "name": "Bricy (ORE)"
            },
            {
                "id": "ANG",
                "name": "Brie Champniers (ANG)"
            },
            {
                "id": "BMC",
                "name": "Brigham City (BMC)"
            },
            {
                "id": "BNE",
                "name": "Brisbane Intl (BNE)"
            },
            {
                "id": "BRS",
                "name": "Bristol (BRS)"
            },
            {
                "id": "FZO",
                "name": "Bristol Filton (FZO)"
            },
            {
                "id": "BZZ",
                "name": "Brize Norton (BZZ)"
            },
            {
                "id": "YBT",
                "name": "Brochet Airport (YBT)"
            },
            {
                "id": "BHQ",
                "name": "Broken Hill Airport (BHQ)"
            },
            {
                "id": "BMA",
                "name": "Bromma (BMA)"
            },
            {
                "id": "ZBM",
                "name": "Bromont Airport (ZBM)"
            },
            {
                "id": "LYN",
                "name": "Bron (LYN)"
            },
            {
                "id": "BNN",
                "name": "Bronnoy (BNN)"
            },
            {
                "id": "BKX",
                "name": "Brookings Regional Airport (BKX)"
            },
            {
                "id": "RMY",
                "name": "Brooks Field Airport (RMY)"
            },
            {
                "id": "BME",
                "name": "Broome (BME)"
            },
            {
                "id": "SDM",
                "name": "Brown Field Municipal Airport (SDM)"
            },
            {
                "id": "BRO",
                "name": "Brownsville South Padre Island Intl (BRO)"
            },
            {
                "id": "BWN",
                "name": "Brunei Intl (BWN)"
            },
            {
                "id": "BQK",
                "name": "Brunswick Golden Isles Airport (BQK)"
            },
            {
                "id": "BHG",
                "name": "Brus Laguna Airport (BHG)"
            },
            {
                "id": "ZYR",
                "name": "Brussels Gare du Midi (ZYR)"
            },
            {
                "id": "BRU",
                "name": "Brussels Natl (BRU)"
            },
            {
                "id": "CRL",
                "name": "Brussels South (CRL)"
            },
            {
                "id": "BQB",
                "name": "Brusselton (BQB)"
            },
            {
                "id": "BZK",
                "name": "Bryansk (BZK)"
            },
            {
                "id": "FRN",
                "name": "Bryant Ahp (FRN)"
            },
            {
                "id": "BCE",
                "name": "Bryce Canyon (BCE)"
            },
            {
                "id": "LUW",
                "name": "Bubung (LUW)"
            },
            {
                "id": "CCR",
                "name": "Buchanan Field Airport (CCR)"
            },
            {
                "id": "BUH",
                "name": "Buchloe BF (BUH)"
            },
            {
                "id": "KWA",
                "name": "Bucholz Aaf (KWA)"
            },
            {
                "id": "BXK",
                "name": "Buckeye Municipal Airport (BXK)"
            },
            {
                "id": "BKC",
                "name": "Buckland Airport (BKC)"
            },
            {
                "id": "BKF",
                "name": "Buckley Afb (BKF)"
            },
            {
                "id": "YVT",
                "name": "Buffalo Narrows (YVT)"
            },
            {
                "id": "BUF",
                "name": "Buffalo Niagara Intl (BUF)"
            },
            {
                "id": "BFO",
                "name": "Buffalo Range (BFO)"
            },
            {
                "id": "UUA",
                "name": "Bugulma Airport (UUA)"
            },
            {
                "id": "BJM",
                "name": "Bujumbura Intl (BJM)"
            },
            {
                "id": "BUA",
                "name": "Buka Airport (BUA)"
            },
            {
                "id": "BKY",
                "name": "Bukavu Kavumu (BKY)"
            },
            {
                "id": "BHK",
                "name": "Bukhara (BHK)"
            },
            {
                "id": "BKZ",
                "name": "Bukoba Airport (BKZ)"
            },
            {
                "id": "UGA",
                "name": "Bulgan Airport (UGA)"
            },
            {
                "id": "BDB",
                "name": "Bundaberg (BDB)"
            },
            {
                "id": "BUX",
                "name": "Bunia (BUX)"
            },
            {
                "id": "BXO",
                "name": "Buochs Airport (BXO)"
            },
            {
                "id": "BMV",
                "name": "Buon Ma Thuot Airport (BMV)"
            },
            {
                "id": "BUO",
                "name": "Burao Airport (BUO)"
            },
            {
                "id": "BVV",
                "name": "Burevestnik Airport (BVV)"
            },
            {
                "id": "BOJ",
                "name": "Burgas (BOJ)"
            },
            {
                "id": "RGS",
                "name": "Burgos Airport (RGS)"
            },
            {
                "id": "BFV",
                "name": "Buri Ram (BFV)"
            },
            {
                "id": "BKL",
                "name": "Burke Lakefront Airport (BKL)"
            },
            {
                "id": "BUC",
                "name": "Burketown Airport (BUC)"
            },
            {
                "id": "BTV",
                "name": "Burlington Intl (BTV)"
            },
            {
                "id": "BUY",
                "name": "Burlington-Alamance Regional Airport (BUY)"
            },
            {
                "id": "K27",
                "name": "Burrello-Mechanicville Airport (K27)"
            },
            {
                "id": "BTZ",
                "name": "Bursa Airport (BTZ)"
            },
            {
                "id": "YDB",
                "name": "Burwash (YDB)"
            },
            {
                "id": "BUZ",
                "name": "Bushehr (BUZ)"
            },
            {
                "id": "USU",
                "name": "Busuanga (USU)"
            },
            {
                "id": "BBG",
                "name": "Butaritari Atoll Airport (BBG)"
            },
            {
                "id": "YKZ",
                "name": "Buttonville Muni (YKZ)"
            },
            {
                "id": "FCS",
                "name": "Butts Aaf (FCS)"
            },
            {
                "id": "BXU",
                "name": "Butuan (BXU)"
            },
            {
                "id": "BZG",
                "name": "Bydgoszcz Ignacy Jan Paderewski Airport (BZG)"
            },
            {
                "id": "BKA",
                "name": "Bykovo (BKA)"
            },
            {
                "id": "CBH",
                "name": "Béchar Boudghene Ben Ali Lotfi Airport (CBH)"
            },
            {
                "id": "VSA",
                "name": "C P A Carlos Rovirosa Intl (VSA)"
            },
            {
                "id": "CAH",
                "name": "Ca Mau (CAH)"
            },
            {
                "id": "LHC",
                "name": "Caballococha Airport (LHC)"
            },
            {
                "id": "CAB",
                "name": "Cabinda (CAB)"
            },
            {
                "id": "CFB",
                "name": "Cabo Frio International Airport (CFB)"
            },
            {
                "id": "TNO",
                "name": "Cabo Velas Airport (TNO)"
            },
            {
                "id": "ITB",
                "name": "Cachimbo (ITB)"
            },
            {
                "id": "TPP",
                "name": "Cadete Guillermo Del Castillo Paredes (TPP)"
            },
            {
                "id": "COO",
                "name": "Cadjehoun (COO)"
            },
            {
                "id": "CGY",
                "name": "Cagayan De Oro (CGY)"
            },
            {
                "id": "CNS",
                "name": "Cairns Intl (CNS)"
            },
            {
                "id": "CAI",
                "name": "Cairo Intl (CAI)"
            },
            {
                "id": "70J",
                "name": "Cairo-Grady County Airport (70J)"
            },
            {
                "id": "CBQ",
                "name": "Calabar (CBQ)"
            },
            {
                "id": "CQF",
                "name": "Calais Dunkerque (CQF)"
            },
            {
                "id": "CYP",
                "name": "Calbayog Airport (CYP)"
            },
            {
                "id": "CLV",
                "name": "Caldas Novas (CLV)"
            },
            {
                "id": "CDW",
                "name": "Caldwell Essex County Airport (CDW)"
            },
            {
                "id": "CVI",
                "name": "Caleta Olivia (CVI)"
            },
            {
                "id": "CXL",
                "name": "Calexico Intl (CXL)"
            },
            {
                "id": "YYC",
                "name": "Calgary Intl (YYC)"
            },
            {
                "id": "YBW",
                "name": "Calgary Springbank Airport (YBW)"
            },
            {
                "id": "CCJ",
                "name": "Calicut (CCJ)"
            },
            {
                "id": "CXR",
                "name": "Cam Ranh Airport (CXR)"
            },
            {
                "id": "CBG",
                "name": "Cambridge (CBG)"
            },
            {
                "id": "YCB",
                "name": "Cambridge Bay (YCB)"
            },
            {
                "id": "CDI",
                "name": "Cambridge Municipal Airport (CDI)"
            },
            {
                "id": "CDU",
                "name": "Camden (CDU)"
            },
            {
                "id": "CGM",
                "name": "Camiguin Airport (CGM)"
            },
            {
                "id": "CUC",
                "name": "Camilo Daza (CUC)"
            },
            {
                "id": "LOH",
                "name": "Camilo Ponce Enriquez Airport (LOH)"
            },
            {
                "id": "ATT",
                "name": "Camp Mabry Austin City (ATT)"
            },
            {
                "id": "HOP",
                "name": "Campbell Aaf (HOP)"
            },
            {
                "id": "YBL",
                "name": "Campbell River (YBL)"
            },
            {
                "id": "CAL",
                "name": "Campbeltown Airport (CAL)"
            },
            {
                "id": "CMP",
                "name": "Campo Alegre Airport (CMP)"
            },
            {
                "id": "AJA",
                "name": "Campo Dell Oro (AJA)"
            },
            {
                "id": "CXJ",
                "name": "Campo Dos Bugres (CXJ)"
            },
            {
                "id": "QPS",
                "name": "Campo Fontenelle (QPS)"
            },
            {
                "id": "CGR",
                "name": "Campo Grande (CGR)"
            },
            {
                "id": "YXC",
                "name": "Canadian Rockies Intl (YXC)"
            },
            {
                "id": "CAJ",
                "name": "Canaima (CAJ)"
            },
            {
                "id": "CKZ",
                "name": "Canakkale Airport (CKZ)"
            },
            {
                "id": "ZOS",
                "name": "Canal Bajo Carlos Hott Siebert (ZOS)"
            },
            {
                "id": "CBR",
                "name": "Canberra (CBR)"
            },
            {
                "id": "CUN",
                "name": "Cancun Intl (CUN)"
            },
            {
                "id": "DCF",
                "name": "Canefield (DCF)"
            },
            {
                "id": "CVS",
                "name": "Cannon Afb (CVS)"
            },
            {
                "id": "CIW",
                "name": "Canouan (CIW)"
            },
            {
                "id": "CIS",
                "name": "Canton (CIS)"
            },
            {
                "id": "CNY",
                "name": "Canyonlands Field (CNY)"
            },
            {
                "id": "PCL",
                "name": "Cap Fap David Abenzur Rengifo Intl (PCL)"
            },
            {
                "id": "CAP",
                "name": "Cap Haitien Intl (CAP)"
            },
            {
                "id": "CHX",
                "name": "Cap Manuel Nino Intl (CHX)"
            },
            {
                "id": "CSK",
                "name": "Cap Skiring (CSK)"
            },
            {
                "id": "YTE",
                "name": "Cape Dorset (YTE)"
            },
            {
                "id": "CGI",
                "name": "Cape Girardeau Regional Airport (CGI)"
            },
            {
                "id": "LUR",
                "name": "Cape Lisburne Lrrs (LUR)"
            },
            {
                "id": "WWD",
                "name": "Cape May Co (WWD)"
            },
            {
                "id": "EHM",
                "name": "Cape Newenham Lrrs (EHM)"
            },
            {
                "id": "CPA",
                "name": "Cape Palmas Airport (CPA)"
            },
            {
                "id": "CZF",
                "name": "Cape Romanzof Lrrs (CZF)"
            },
            {
                "id": "CPT",
                "name": "Cape Town Intl (CPT)"
            },
            {
                "id": "LAN",
                "name": "Capital City (LAN)"
            },
            {
                "id": "CXY",
                "name": "Capital City Airport (CXY)"
            },
            {
                "id": "FFT",
                "name": "Capital City Airport (FFT)"
            },
            {
                "id": "PEK",
                "name": "Capital Intl (PEK)"
            },
            {
                "id": "TRU",
                "name": "Capitan Carlos Martinez De Pinillos (TRU)"
            },
            {
                "id": "PDP",
                "name": "Capitan Corbeta C A Curbelo International Airport (PDP)"
            },
            {
                "id": "PIU",
                "name": "Capitan Fap Guillermo Concha Iberico (PIU)"
            },
            {
                "id": "TYL",
                "name": "Capitan Montes (TYL)"
            },
            {
                "id": "POI",
                "name": "Capitan Nicolas Rojas (POI)"
            },
            {
                "id": "TJA",
                "name": "Capitan Oriel Lea Plaza (TJA)"
            },
            {
                "id": "SRJ",
                "name": "Capitán Av. German Quiroga G. Airport (SRJ)"
            },
            {
                "id": "RIB",
                "name": "Capitán Av. Selin Zeitun Lopez Airport (RIB)"
            },
            {
                "id": "GYA",
                "name": "Capitán de Av. Emilio Beltrán Airport (GYA)"
            },
            {
                "id": "NAP",
                "name": "Capodichino (NAP)"
            },
            {
                "id": "CIX",
                "name": "Capt Jose A Quinones Gonzales Intl (CIX)"
            },
            {
                "id": "PLP",
                "name": "Captain Ramon Xatruch Airport (PLP)"
            },
            {
                "id": "CYW",
                "name": "Captain Rogelio Castillo National Airport (CYW)"
            },
            {
                "id": "CPB",
                "name": "Capurgana Airport (CPB)"
            },
            {
                "id": "CKS",
                "name": "Carajas Airport (CKS)"
            },
            {
                "id": "CSB",
                "name": "Caransebes (CSB)"
            },
            {
                "id": "CAF",
                "name": "Carauari Airport (CAF)"
            },
            {
                "id": "CRQ",
                "name": "Caravelas (CRQ)"
            },
            {
                "id": "PUC",
                "name": "Carbon County Regional-Buck Davis Field (PUC)"
            },
            {
                "id": "DNZ",
                "name": "Cardak (DNZ)"
            },
            {
                "id": "CWL",
                "name": "Cardiff (CWL)"
            },
            {
                "id": "CAR",
                "name": "Caribou Muni (CAR)"
            },
            {
                "id": "CAX",
                "name": "Carlisle (CAX)"
            },
            {
                "id": "CFC",
                "name": "Carlos Alberto da Costa Neves Airport (CFC)"
            },
            {
                "id": "PUQ",
                "name": "Carlos Ibanez Del Campo Intl (PUQ)"
            },
            {
                "id": "BYM",
                "name": "Carlos Manuel De Cespedes (BYM)"
            },
            {
                "id": "CVQ",
                "name": "Carnarvon Airport (CVQ)"
            },
            {
                "id": "CLN",
                "name": "Carolina (CLN)"
            },
            {
                "id": "CFR",
                "name": "Carpiquet (CFR)"
            },
            {
                "id": "MVD",
                "name": "Carrasco Intl (MVD)"
            },
            {
                "id": "CCP",
                "name": "Carriel Sur Intl (CCP)"
            },
            {
                "id": "RIK",
                "name": "Carrillo Airport (RIK)"
            },
            {
                "id": "VPC",
                "name": "Cartersville Airport (VPC)"
            },
            {
                "id": "TUN",
                "name": "Carthage (TUN)"
            },
            {
                "id": "YRF",
                "name": "Cartwright Airport (YRF)"
            },
            {
                "id": "CAU",
                "name": "Caruaru Airport (CAU)"
            },
            {
                "id": "LRM",
                "name": "Casa De Campo Intl (LRM)"
            },
            {
                "id": "CGZ",
                "name": "Casa Grande Municipal Airport (CGZ)"
            },
            {
                "id": "BDS",
                "name": "Casale (BDS)"
            },
            {
                "id": "CAC",
                "name": "Cascavel (CAC)"
            },
            {
                "id": "PYH",
                "name": "Casique Aramare (PYH)"
            },
            {
                "id": "CXI",
                "name": "Cassidy Intl (CXI)"
            },
            {
                "id": "MER",
                "name": "Castle (MER)"
            },
            {
                "id": "YCG",
                "name": "Castlegar (YCG)"
            },
            {
                "id": "HPH",
                "name": "Cat Bi International Airport (HPH)"
            },
            {
                "id": "YAC",
                "name": "Cat Lake Airport (YAC)"
            },
            {
                "id": "TCE",
                "name": "Cataloi (TCE)"
            },
            {
                "id": "CTC",
                "name": "Catamarca (CTC)"
            },
            {
                "id": "CTA",
                "name": "Catania Fontanarossa (CTA)"
            },
            {
                "id": "IGR",
                "name": "Cataratas Del Iguazu (IGR)"
            },
            {
                "id": "IGU",
                "name": "Cataratas Intl (IGU)"
            },
            {
                "id": "CRM",
                "name": "Catarman National Airport (CRM)"
            },
            {
                "id": "CBT",
                "name": "Catumbela Airport (CBT)"
            },
            {
                "id": "CYZ",
                "name": "Cauayan Airport (CYZ)"
            },
            {
                "id": "LQM",
                "name": "Caucaya Airport (LQM)"
            },
            {
                "id": "AVN",
                "name": "Caumont (AVN)"
            },
            {
                "id": "CNM",
                "name": "Cavern City Air Terminal (CNM)"
            },
            {
                "id": "AAJ",
                "name": "Cayana Airstrip (AAJ)"
            },
            {
                "id": "CUK",
                "name": "Caye Caulker Airport (CUK)"
            },
            {
                "id": "CYC",
                "name": "Caye Chapel Airport (CYC)"
            },
            {
                "id": "CCC",
                "name": "Cayo Coco Airport (CCC)"
            },
            {
                "id": "NZC",
                "name": "Cecil Field (NZC)"
            },
            {
                "id": "CDC",
                "name": "Cedar City Rgnl (CDC)"
            },
            {
                "id": "CID",
                "name": "Cedar Rapids (CID)"
            },
            {
                "id": "CDK",
                "name": "CedarKey (CDK)"
            },
            {
                "id": "CED",
                "name": "Ceduna Airport (CED)"
            },
            {
                "id": "ZCN",
                "name": "Celle (ZCN)"
            },
            {
                "id": "APA",
                "name": "Centennial (APA)"
            },
            {
                "id": "QRH",
                "name": "Centraal (QRH)"
            },
            {
                "id": "ZWE",
                "name": "Centraal (ZWE)"
            },
            {
                "id": "RTW",
                "name": "Central (RTW)"
            },
            {
                "id": "ZGH",
                "name": "Central (ZGH)"
            },
            {
                "id": "CEM",
                "name": "Central Airport (CEM)"
            },
            {
                "id": "BMI",
                "name": "Central Illinois Rgnl (BMI)"
            },
            {
                "id": "GRI",
                "name": "Central Nebraska Regional Airport (GRI)"
            },
            {
                "id": "YMY",
                "name": "Central Railway Station (YMY)"
            },
            {
                "id": "QYX",
                "name": "Central Station (QYX)"
            },
            {
                "id": "XEV",
                "name": "Central Station (XEV)"
            },
            {
                "id": "ZGG",
                "name": "Central Station (ZGG)"
            },
            {
                "id": "CWA",
                "name": "Central Wisconsin (CWA)"
            },
            {
                "id": "PYP",
                "name": "Centre-Piedmont-Cherokee County Regional Airport (PYP)"
            },
            {
                "id": "ANF",
                "name": "Cerro Moreno Intl (ANF)"
            },
            {
                "id": "CES",
                "name": "Cessnock Airport (CES)"
            },
            {
                "id": "JCU",
                "name": "Ceuta Heliport (JCU)"
            },
            {
                "id": "XBK",
                "name": "Ceyzeriat (XBK)"
            },
            {
                "id": "VAF",
                "name": "Chabeuil (VAF)"
            },
            {
                "id": "ARI",
                "name": "Chacalluta (ARI)"
            },
            {
                "id": "CHH",
                "name": "Chachapoyas (CHH)"
            },
            {
                "id": "ATF",
                "name": "Chachoan (ATF)"
            },
            {
                "id": "CDR",
                "name": "Chadron Municipal Airport (CDR)"
            },
            {
                "id": "CCN",
                "name": "Chaghcharan Airport (CCN)"
            },
            {
                "id": "ZBR",
                "name": "Chah Bahar (ZBR)"
            },
            {
                "id": "WCH",
                "name": "Chaiten (WCH)"
            },
            {
                "id": "ISB",
                "name": "Chaklala (ISB)"
            },
            {
                "id": "CIK",
                "name": "Chalkyitsik Airport (CIK)"
            },
            {
                "id": "RHE",
                "name": "Champagne (RHE)"
            },
            {
                "id": "CMI",
                "name": "Champaign (CMI)"
            },
            {
                "id": "XCD",
                "name": "Champforgeuil (XCD)"
            },
            {
                "id": "YKN",
                "name": "Chan Gurney (YKN)"
            },
            {
                "id": "IXC",
                "name": "Chandigarh (IXC)"
            },
            {
                "id": "BDP",
                "name": "Chandragadhi Airport (BDP)"
            },
            {
                "id": "NBS",
                "name": "Changbaishan Airport (NBS)"
            },
            {
                "id": "KHN",
                "name": "Changbei Intl (KHN)"
            },
            {
                "id": "CGQ",
                "name": "Changchun (CGQ)"
            },
            {
                "id": "CGD",
                "name": "Changde Airport (CGD)"
            },
            {
                "id": "CNI",
                "name": "Changhai (CNI)"
            },
            {
                "id": "SIN",
                "name": "Changi Intl (SIN)"
            },
            {
                "id": "FOC",
                "name": "Changle (FOC)"
            },
            {
                "id": "CIH",
                "name": "Changzhi Airport (CIH)"
            },
            {
                "id": "CZX",
                "name": "Changzhou (CZX)"
            },
            {
                "id": "WUZ",
                "name": "Changzhoudao Airport (WUZ)"
            },
            {
                "id": "CHG",
                "name": "Chaoyang Airport (CHG)"
            },
            {
                "id": "LEC",
                "name": "Chapada Diamantina Airport (LEC)"
            },
            {
                "id": "YMT",
                "name": "Chapais Airport (YMT)"
            },
            {
                "id": "XAP",
                "name": "Chapeco (XAP)"
            },
            {
                "id": "YLD",
                "name": "Chapleau (YLD)"
            },
            {
                "id": "CDG",
                "name": "Charles De Gaulle (CDG)"
            },
            {
                "id": "STS",
                "name": "Charles M Schulz Sonoma Co (STS)"
            },
            {
                "id": "CHS",
                "name": "Charleston Afb Intl (CHS)"
            },
            {
                "id": "CTL",
                "name": "Charleville (CTL)"
            },
            {
                "id": "CVX",
                "name": "Charlevoix Municipal Airport (CVX)"
            },
            {
                "id": "YCL",
                "name": "Charlo (YCL)"
            },
            {
                "id": "PGD",
                "name": "Charlotte County-Punta Gorda Airport (PGD)"
            },
            {
                "id": "CLT",
                "name": "Charlotte Douglas Intl (CLT)"
            },
            {
                "id": "CHO",
                "name": "Charlottesville-Albemarle (CHO)"
            },
            {
                "id": "YYG",
                "name": "Charlottetown (YYG)"
            },
            {
                "id": "VHY",
                "name": "Charmeil (VHY)"
            },
            {
                "id": "QNX",
                "name": "Charnay (QNX)"
            },
            {
                "id": "CNG",
                "name": "Chateaubernard (CNG)"
            },
            {
                "id": "CHT",
                "name": "Chatham Islands (CHT)"
            },
            {
                "id": "CYM",
                "name": "Chatham Seaplane Base (CYM)"
            },
            {
                "id": "HRJ",
                "name": "Chaurjhari (HRJ)"
            },
            {
                "id": "DKK",
                "name": "Chautauqua County-Dunkirk Airport (DKK)"
            },
            {
                "id": "JHW",
                "name": "Chautauqua County-Jamestown (JHW)"
            },
            {
                "id": "CSY",
                "name": "Cheboksary Airport (CSY)"
            },
            {
                "id": "GEO",
                "name": "Cheddi Jagan Intl (GEO)"
            },
            {
                "id": "CYF",
                "name": "Chefornak Airport (CYF)"
            },
            {
                "id": "CLS",
                "name": "Chehalis-Centralia (CLS)"
            },
            {
                "id": "TEE",
                "name": "Cheikh Larbi Tebessi (TEE)"
            },
            {
                "id": "49X",
                "name": "Chemehuevi Valley (49X)"
            },
            {
                "id": "NCN",
                "name": "Chenega Bay Airport (NCN)"
            },
            {
                "id": "MAA",
                "name": "Chennai Intl (MAA)"
            },
            {
                "id": "CJJ",
                "name": "Cheongju International Airport (CJJ)"
            },
            {
                "id": "CEE",
                "name": "Cherepovets Airport (CEE)"
            },
            {
                "id": "AHU",
                "name": "Cherif El Idrissi (AHU)"
            },
            {
                "id": "CKC",
                "name": "Cherkassy (CKC)"
            },
            {
                "id": "CEJ",
                "name": "Chernigov (CEJ)"
            },
            {
                "id": "CWC",
                "name": "Chernivtsi International Airport (CWC)"
            },
            {
                "id": "KHE",
                "name": "Chernobayevka Airport (KHE)"
            },
            {
                "id": "47A",
                "name": "Cherokee County Airport (47A)"
            },
            {
                "id": "TVC",
                "name": "Cherry Capital Airport (TVC)"
            },
            {
                "id": "NKT",
                "name": "Cherry Point Mcas (NKT)"
            },
            {
                "id": "CYX",
                "name": "Cherskiy Airport (CYX)"
            },
            {
                "id": "VOZ",
                "name": "Chertovitskoye (VOZ)"
            },
            {
                "id": "CTH",
                "name": "Chester County G O Carlson Airport (CTH)"
            },
            {
                "id": "YCS",
                "name": "Chesterfield Inlet Airport (YCS)"
            },
            {
                "id": "CTM",
                "name": "Chetumal Intl (CTM)"
            },
            {
                "id": "VAK",
                "name": "Chevak Airport (VAK)"
            },
            {
                "id": "YHR",
                "name": "Chevery Airport (YHR)"
            },
            {
                "id": "CYS",
                "name": "Cheyenne Rgnl Jerry Olson Fld (CYS)"
            },
            {
                "id": "BOM",
                "name": "Chhatrapati Shivaji Intl (BOM)"
            },
            {
                "id": "CNX",
                "name": "Chiang Mai Intl (CNX)"
            },
            {
                "id": "CEI",
                "name": "Chiang Rai Intl (CEI)"
            },
            {
                "id": "CYI",
                "name": "Chiayi (CYI)"
            },
            {
                "id": "PWK",
                "name": "Chicago Executive (PWK)"
            },
            {
                "id": "MDW",
                "name": "Chicago Midway Intl (MDW)"
            },
            {
                "id": "ORD",
                "name": "Chicago Ohare Intl (ORD)"
            },
            {
                "id": "RFD",
                "name": "Chicago Rockford International Airport  (RFD)"
            },
            {
                "id": "CIC",
                "name": "Chico Muni (CIC)"
            },
            {
                "id": "CIF",
                "name": "Chifeng Airport (CIF)"
            },
            {
                "id": "KBW",
                "name": "Chignik Bay Seaplane Base (KBW)"
            },
            {
                "id": "KCL",
                "name": "Chignik Lagoon Airport (KCL)"
            },
            {
                "id": "KCQ",
                "name": "Chignik Lake Airport (KCQ)"
            },
            {
                "id": "CDS",
                "name": "Childress Muni (CDS)"
            },
            {
                "id": "CCH",
                "name": "Chile Chico (CCH)"
            },
            {
                "id": "BLZ",
                "name": "Chileka Intl (BLZ)"
            },
            {
                "id": "YCW",
                "name": "Chilliwack (YCW)"
            },
            {
                "id": "CMU",
                "name": "Chimbu Airport (CMU)"
            },
            {
                "id": "VPY",
                "name": "Chimoio Airport (VPY)"
            },
            {
                "id": "TRR",
                "name": "China Bay (TRR)"
            },
            {
                "id": "NID",
                "name": "China Lake Naws (NID)"
            },
            {
                "id": "CCL",
                "name": "Chinchilla (CCL)"
            },
            {
                "id": "RMQ",
                "name": "Ching Chuang Kang (RMQ)"
            },
            {
                "id": "ULN",
                "name": "Chinggis Khaan Intl (ULN)"
            },
            {
                "id": "E91",
                "name": "Chinle Municipal Airport (E91)"
            },
            {
                "id": "JKH",
                "name": "Chios (JKH)"
            },
            {
                "id": "CIP",
                "name": "Chipata Airport (CIP)"
            },
            {
                "id": "CIU",
                "name": "Chippewa County International Airport (CIU)"
            },
            {
                "id": "EAU",
                "name": "Chippewa Valley Regional Airport (EAU)"
            },
            {
                "id": "CZN",
                "name": "Chisana Airport (CZN)"
            },
            {
                "id": "YKU",
                "name": "Chisasibi Airport (YKU)"
            },
            {
                "id": "HIB",
                "name": "Chisholm Hibbing (HIB)"
            },
            {
                "id": "KIV",
                "name": "Chisinau Intl (KIV)"
            },
            {
                "id": "SPK",
                "name": "Chitose (SPK)"
            },
            {
                "id": "CJL",
                "name": "Chitral Airport (CJL)"
            },
            {
                "id": "CKL",
                "name": "Chkalovsky Airport (CKL)"
            },
            {
                "id": "COQ",
                "name": "Choibalsan Airport (COQ)"
            },
            {
                "id": "CHY",
                "name": "Choiseul Bay Airport (CHY)"
            },
            {
                "id": "CKH",
                "name": "Chokurdakh Airport (CKH)"
            },
            {
                "id": "RGO",
                "name": "Chongjin Airport (RGO)"
            },
            {
                "id": "YZR",
                "name": "Chris Hadfield (YZR)"
            },
            {
                "id": "CHC",
                "name": "Christchurch Intl (CHC)"
            },
            {
                "id": "SSB",
                "name": "Christiansted Harbor Seaplane Base (SSB)"
            },
            {
                "id": "XCH",
                "name": "Christmas Island (XCH)"
            },
            {
                "id": "VCL",
                "name": "Chu Lai (VCL)"
            },
            {
                "id": "CHU",
                "name": "Chuathbaluk Airport (CHU)"
            },
            {
                "id": "CCZ",
                "name": "Chub Cay (CCZ)"
            },
            {
                "id": "NGO",
                "name": "Chubu Centrair Intl (NGO)"
            },
            {
                "id": "CNN",
                "name": "Chulman (CNN)"
            },
            {
                "id": "CJM",
                "name": "Chumphon (CJM)"
            },
            {
                "id": "YYQ",
                "name": "Churchill (YYQ)"
            },
            {
                "id": "ZUM",
                "name": "Churchill Falls Airport (ZUM)"
            },
            {
                "id": "TKK",
                "name": "Chuuk Intl (TKK)"
            },
            {
                "id": "CIA",
                "name": "Ciampino (CIA)"
            },
            {
                "id": "STI",
                "name": "Cibao Intl (STI)"
            },
            {
                "id": "ICI",
                "name": "Cicia Airport (ICI)"
            },
            {
                "id": "IGL",
                "name": "Cigli (IGL)"
            },
            {
                "id": "CMJ",
                "name": "Cimei Airport (CMJ)"
            },
            {
                "id": "LUK",
                "name": "Cincinnati Muni Lunken Fld (LUK)"
            },
            {
                "id": "CVG",
                "name": "Cincinnati Northern Kentucky Intl (CVG)"
            },
            {
                "id": "IRC",
                "name": "Circle City Airport (IRC)"
            },
            {
                "id": "LCY",
                "name": "City (LCY)"
            },
            {
                "id": "YTZ",
                "name": "City Centre (YTZ)"
            },
            {
                "id": "COS",
                "name": "City Of Colorado Springs Muni (COS)"
            },
            {
                "id": "LDY",
                "name": "City of Derry (LDY)"
            },
            {
                "id": "CBL",
                "name": "Ciudad Bolivar (CBL)"
            },
            {
                "id": "CUA",
                "name": "Ciudad Constitución Airport (CUA)"
            },
            {
                "id": "CME",
                "name": "Ciudad Del Carmen Intl (CME)"
            },
            {
                "id": "AGT",
                "name": "Ciudad del Este (AGT)"
            },
            {
                "id": "CEN",
                "name": "Ciudad Obregon Intl (CEN)"
            },
            {
                "id": "CQM",
                "name": "Ciudad Real Central Airport (CQM)"
            },
            {
                "id": "CKV",
                "name": "Clarksville-Montgomery County Regional Airport (CKV)"
            },
            {
                "id": "4A7",
                "name": "Clayton County Tara Field (4A7)"
            },
            {
                "id": "Z84",
                "name": "Clear (Z84)"
            },
            {
                "id": "CLC",
                "name": "Clear Lake Metroport (CLC)"
            },
            {
                "id": "CLW",
                "name": "Clearwater Air Park (CLW)"
            },
            {
                "id": "CEU",
                "name": "Clemson (CEU)"
            },
            {
                "id": "CLE",
                "name": "Cleveland Hopkins Intl (CLE)"
            },
            {
                "id": "CWI",
                "name": "Clinton Municipal (CWI)"
            },
            {
                "id": "CNJ",
                "name": "Cloncurry Airport (CNJ)"
            },
            {
                "id": "CVN",
                "name": "Clovis Muni (CVN)"
            },
            {
                "id": "1CS",
                "name": "Clow International Airport (1CS)"
            },
            {
                "id": "CMK",
                "name": "Club Makokola Airport (CMK)"
            },
            {
                "id": "CLJ",
                "name": "Cluj Napoca (CLJ)"
            },
            {
                "id": "YCY",
                "name": "Clyde River (YCY)"
            },
            {
                "id": "VCS",
                "name": "Co Ong Airport (VCS)"
            },
            {
                "id": "CIZ",
                "name": "Coari Airport (CIZ)"
            },
            {
                "id": "CBV",
                "name": "Coban (CBV)"
            },
            {
                "id": "CAZ",
                "name": "Cobar Airport (CAZ)"
            },
            {
                "id": "RYY",
                "name": "Cobb County Airport-Mc Collum Field (RYY)"
            },
            {
                "id": "COK",
                "name": "Cochin (COK)"
            },
            {
                "id": "YCN",
                "name": "Cochrane (YCN)"
            },
            {
                "id": "CNC",
                "name": "Coconut Island Airport (CNC)"
            },
            {
                "id": "CCK",
                "name": "Cocos Keeling Island Airport (CCK)"
            },
            {
                "id": "CSC",
                "name": "Codela Airport (CSC)"
            },
            {
                "id": "BBQ",
                "name": "Codrington Airport (BBQ)"
            },
            {
                "id": "CUQ",
                "name": "Coen Airport (CUQ)"
            },
            {
                "id": "KCC",
                "name": "Coffman Cove Seaplane Base (KCC)"
            },
            {
                "id": "CFS",
                "name": "Coffs Harbour (CFS)"
            },
            {
                "id": "CJB",
                "name": "Coimbatore (CJB)"
            },
            {
                "id": "CDB",
                "name": "Cold Bay (CDB)"
            },
            {
                "id": "YOD",
                "name": "Cold Lake (YOD)"
            },
            {
                "id": "CXF",
                "name": "Coldfoot Airport (CXF)"
            },
            {
                "id": "DET",
                "name": "Coleman A Young Muni (DET)"
            },
            {
                "id": "CLQ",
                "name": "Colima (CLQ)"
            },
            {
                "id": "COL",
                "name": "Coll Airport (COL)"
            },
            {
                "id": "TKI",
                "name": "Collin County Regional Airport at Mc Kinney (TKI)"
            },
            {
                "id": "QKL",
                "name": "Cologne Railway (QKL)"
            },
            {
                "id": "RML",
                "name": "Colombo Ratmalana (RML)"
            },
            {
                "id": "CRI",
                "name": "Colonel Hill Airport (CRI)"
            },
            {
                "id": "OLN",
                "name": "Colonia Sarmiento (OLN)"
            },
            {
                "id": "CSA",
                "name": "Colonsay Airport (CSA)"
            },
            {
                "id": "A50",
                "name": "Colorado Springs East (A50)"
            },
            {
                "id": "HCC",
                "name": "Columbia County (HCC)"
            },
            {
                "id": "CAE",
                "name": "Columbia Metropolitan (CAE)"
            },
            {
                "id": "COU",
                "name": "Columbia Rgnl (COU)"
            },
            {
                "id": "CBM",
                "name": "Columbus Afb (CBM)"
            },
            {
                "id": "CSG",
                "name": "Columbus Metropolitan Airport (CSG)"
            },
            {
                "id": "YCK",
                "name": "Colville Lake Airport (YCK)"
            },
            {
                "id": "BHI",
                "name": "Comandante Espora (BHI)"
            },
            {
                "id": "ATA",
                "name": "Comandante Fap German Arias Graziani (ATA)"
            },
            {
                "id": "BGX",
                "name": "Comandante Gustavo Kraemer (BGX)"
            },
            {
                "id": "DSE",
                "name": "Combolcha Airport (DSE)"
            },
            {
                "id": "JCO",
                "name": "Comino Airport (JCO)"
            },
            {
                "id": "CIY",
                "name": "Comiso (CIY)"
            },
            {
                "id": "COC",
                "name": "Comodoro Pierrestegui (COC)"
            },
            {
                "id": "CRD",
                "name": "Comodoro Rivadavia (CRD)"
            },
            {
                "id": "YQQ",
                "name": "Comox (YQQ)"
            },
            {
                "id": "CKY",
                "name": "Conakry (CKY)"
            },
            {
                "id": "CDJ",
                "name": "Conceicao Do Araguaia (CDJ)"
            },
            {
                "id": "CON",
                "name": "Concord Municipal (CON)"
            },
            {
                "id": "CCI",
                "name": "Concordia Airport (CCI)"
            },
            {
                "id": "WSD",
                "name": "Condron Aaf (WSD)"
            },
            {
                "id": "CFO",
                "name": "Confresa Airport (CFO)"
            },
            {
                "id": "STZ",
                "name": "Confresa Airport (STZ)"
            },
            {
                "id": "COX",
                "name": "Congo Town Airport (COX)"
            },
            {
                "id": "CGH",
                "name": "Congonhas (CGH)"
            },
            {
                "id": "QCY",
                "name": "Coningsby (QCY)"
            },
            {
                "id": "NNR",
                "name": "Connemara Regional Airport (NNR)"
            },
            {
                "id": "COZ",
                "name": "Constanza Airport (COZ)"
            },
            {
                "id": "OTD",
                "name": "Contadora Airport (OTD)"
            },
            {
                "id": "CPD",
                "name": "Coober Pedy Airport (CPD)"
            },
            {
                "id": "COH",
                "name": "Cooch Behar (COH)"
            },
            {
                "id": "CDA",
                "name": "Cooinda (CDA)"
            },
            {
                "id": "CTN",
                "name": "Cooktown Airport (CTN)"
            },
            {
                "id": "P08",
                "name": "Coolidge Municipal Airport (P08)"
            },
            {
                "id": "OOM",
                "name": "Cooma Snowy Mountains Airport (OOM)"
            },
            {
                "id": "COJ",
                "name": "Coonabarabran (COJ)"
            },
            {
                "id": "CNB",
                "name": "Coonamble Airport (CNB)"
            },
            {
                "id": "CPO",
                "name": "Copiapo (CPO)"
            },
            {
                "id": "ODB",
                "name": "Cordoba (ODB)"
            },
            {
                "id": "ORK",
                "name": "Cork (ORK)"
            },
            {
                "id": "POX",
                "name": "Cormeilles En Vexin (POX)"
            },
            {
                "id": "RNI",
                "name": "Corn Island Airport (RNI)"
            },
            {
                "id": "YCC",
                "name": "Cornwall Regional Airport (YCC)"
            },
            {
                "id": "YCT",
                "name": "Coronation (YCT)"
            },
            {
                "id": "TCQ",
                "name": "Coronel Carlos Ciriani Santa Rosa Intl (TCQ)"
            },
            {
                "id": "XMS",
                "name": "Coronel E Carvajal (XMS)"
            },
            {
                "id": "AYP",
                "name": "Coronel Fap Alfredo Mendivil Duarte (AYP)"
            },
            {
                "id": "IQT",
                "name": "Coronel Francisco Secada Vignetta Intl (IQT)"
            },
            {
                "id": "CRP",
                "name": "Corpus Christi Intl (CRP)"
            },
            {
                "id": "NGP",
                "name": "Corpus Christi NAS (NGP)"
            },
            {
                "id": "CNQ",
                "name": "Corrientes (CNQ)"
            },
            {
                "id": "CEZ",
                "name": "Cortez Muni (CEZ)"
            },
            {
                "id": "CMG",
                "name": "Corumba Intl (CMG)"
            },
            {
                "id": "CVU",
                "name": "Corvo Airport (CVU)"
            },
            {
                "id": "CBO",
                "name": "Cotabato (CBO)"
            },
            {
                "id": "NCE",
                "name": "Cote D'Azur (NCE)"
            },
            {
                "id": "OTR",
                "name": "Coto 47 (OTR)"
            },
            {
                "id": "LTX",
                "name": "Cotopaxi International Airport (LTX)"
            },
            {
                "id": "P52",
                "name": "Cottonwood Airport (P52)"
            },
            {
                "id": "COT",
                "name": "Cotulla Lasalle Co (COT)"
            },
            {
                "id": "CFD",
                "name": "Coulter Fld (CFD)"
            },
            {
                "id": "CIL",
                "name": "Council Airport (CIL)"
            },
            {
                "id": "OBE",
                "name": "County (OBE)"
            },
            {
                "id": "CVF",
                "name": "Courchevel Airport (CVF)"
            },
            {
                "id": "CVT",
                "name": "Coventry (CVT)"
            },
            {
                "id": "9A1",
                "name": "Covington Municipal Airport (9A1)"
            },
            {
                "id": "CCO",
                "name": "Coweta County Airport (CCO)"
            },
            {
                "id": "CXB",
                "name": "Coxs Bazar (CXB)"
            },
            {
                "id": "CZM",
                "name": "Cozumel Intl (CZM)"
            },
            {
                "id": "CCV",
                "name": "Craig Cove Airport (CCV)"
            },
            {
                "id": "SEM",
                "name": "Craig Fld (SEM)"
            },
            {
                "id": "CGA",
                "name": "Craig Seaplane Base (CGA)"
            },
            {
                "id": "CRA",
                "name": "Craiova (CRA)"
            },
            {
                "id": "EWN",
                "name": "Craven Co Rgnl (EWN)"
            },
            {
                "id": "INS",
                "name": "Creech Afb (INS)"
            },
            {
                "id": "CSF",
                "name": "Creil (CSF)"
            },
            {
                "id": "CKF",
                "name": "Crisp County Cordele Airport (CKF)"
            },
            {
                "id": "JCA",
                "name": "Croisette Heliport (JCA)"
            },
            {
                "id": "CKD",
                "name": "Crooked Creek Airport (CKD)"
            },
            {
                "id": "CTY",
                "name": "Cross City (CTY)"
            },
            {
                "id": "YCR",
                "name": "Cross Lake - Charlie Sinclair Memorial Airport (YCR)"
            },
            {
                "id": "CRV",
                "name": "Crotone (CRV)"
            },
            {
                "id": "TAB",
                "name": "Crown Point (TAB)"
            },
            {
                "id": "CZS",
                "name": "Cruzeiro do Sul (CZS)"
            },
            {
                "id": "CGC",
                "name": "Crystal River (CGC)"
            },
            {
                "id": "FXO",
                "name": "Cuamba (FXO)"
            },
            {
                "id": "CDP",
                "name": "Cuddapah (CDP)"
            },
            {
                "id": "CPX",
                "name": "Culebra Airport (CPX)"
            },
            {
                "id": "CUL",
                "name": "Culiacan Intl (CUL)"
            },
            {
                "id": "CMA",
                "name": "Cunnamulla Airport (CMA)"
            },
            {
                "id": "CTB",
                "name": "Cut Bank Muni (CTB)"
            },
            {
                "id": "NDZ",
                "name": "Cuxhaven Airport (NDZ)"
            },
            {"id": "CGF", "name": "Cuyahoga County (CGF)"},
            {"id": "CYU", "name": "Cuyo Airport (CYU)"},
            {"id": "STT", "name": "Cyril E King (STT)"},
            {"id": "SIQ", "name": "Dabo (SIQ)"},
            {"id": "DAX", "name": "Dachuan Airport (DAX)"},
            {"id": "TNT", "name": "Dade Collier Training And Transition (TNT)"},
            {"id": "TAE", "name": "Daegu Ab (TAE)"},
            {"id": "VIL", "name": "Dakhla Airport (VIL)"},
            {"id": "DLM", "name": "Dalaman (DLM)"},
            {"id": "DLZ", "name": "Dalanzadgad Airport (DLZ)"},
            {"id": "DLI", "name": "Dalat (DLI)"},
            {"id": "DBA", "name": "Dalbandin Airport (DBA)"},
            {"id": "DHT", "name": "Dalhart Muni (DHT)"},
            {"id": "DLU", "name": "Dali (DLU)"},
            {"id": "RBD", "name": "Dallas Executive Airport (RBD)"},
            {"id": "DFW", "name": "Dallas Fort Worth Intl (DFW)"},
            {"id": "DAL", "name": "Dallas Love Fld (DAL)"},
            {"id": "ZDY", "name": "Dalma Airport (ZDY)"},
            {"id": "DJO", "name": "Daloa (DJO)"},
            {"id": "DNN", "name": "Dalton Municipal Airport (DNN)"},
            {"id": "NMB", "name": "Daman (NMB)"},
            {"id": "DAM", "name": "Damascus Intl (DAM)"},
            {"id": "DAD", "name": "Danang Intl (DAD)"},
            {"id": "DXR", "name": "Danbury Municipal Airport (DXR)"},
            {"id": "DDG", "name": "Dandong (DDG)"},
            {"id": "MSN", "name": "Dane Co Rgnl Truax Fld (MSN)"},
            {"id": "DGA", "name": "Dangriga Airport (DGA)"},
            {"id": "DNL", "name": "Daniel Field Airport (DNL)"},
            {"id": "LIR", "name": "Daniel Oduber Quiros Intl (LIR)"},
            {"id": "TAC", "name": "Daniel Z Romualdez (TAC)"},
            {"id": "DAQ", "name": "Daqing Saertu Airport (DAQ)"},
            {"id": "MQI", "name": "Dare County Regional (MQI)"},
            {"id": "UDG", "name": "Darlington County Jetport (UDG)"},
            {"id": "NLF", "name": "Darnley Island Airport (NLF)"},
            {"id": "DAU", "name": "Daru Airport (DAU)"},
            {"id": "DRW", "name": "Darwin Intl (DRW)"},
            {"id": "TAZ", "name": "Dasoguz Airport (TAZ)"},
            {"id": "DTD", "name": "Datadawai Airport (DTD)"},
            {"id": "DAT", "name": "Datong Airport (DAT)"},
            {"id": "YDN", "name": "Dauphin Barker (YDN)"},
            {"id": "KFE", "name": "Dave Forest Airport (KFE)"},
            {"id": "DWH", "name": "David Wayne Hooks Field (DWH)"},
            {"id": "MKO", "name": "Davis Fld (MKO)"},
            {"id": "DMA", "name": "Davis Monthan Afb (DMA)"},
            {"id": "DWD", "name": "Dawadmi Domestic Airport (DWD)"},
            {"id": "TVY", "name": "Dawei Airport (TVY)"},
            {"id": "YDA", "name": "Dawson City (YDA)"},
            {"id": "GDV", "name": "Dawson Community Airport (GDV)"},
            {"id": "YDQ", "name": "Dawson Creek (YDQ)"},
            {"id": "LLV", "name": "Dawu (LLV)"},
            {"id": "DYG", "name": "Dayong Airport (DYG)"},
            {"id": "MGY", "name": "Dayton-Wright Brothers Airport (MGY)"},
            {"id": "DAB", "name": "Daytona Beach Intl (DAB)"},
            {"id": "DKB", "name": "De Kalb Taylor Municipal Airport (DKB)"},
            {"id": "DHR", "name": "De Kooy (DHR)"},
            {"id": "SCC", "name": "Deadhorse (SCC)"},
            {"id": "LGI", "name": "Deadmans Cay (LGI)"},
            {"id": "YDL", "name": "Dease Lake (YDL)"},
            {"id": "DBM", "name": "Debre Marqos (DBM)"},
            {"id": "DBT", "name": "Debre Tabor Airport (DBT)"},
            {"id": "DEB", "name": "Debrecen (DEB)"},
            {"id": "DEC", "name": "Decatur (DEC)"},
            {"id": "BGE", "name": "Decatur County Industrial Air Park (BGE)"},
            {"id": "DCI", "name": "Decimomannu (DCI)"},
            {"id": "YDF", "name": "Deer Lake (YDF)"},
            {"id": "YVZ", "name": "Deer Lake Airport (YVZ)"},
            {"id": "DVT", "name": "Deer Valley Municipal Airport (DVT)"},
            {"id": "DRG", "name": "Deering Airport (DRG)"},
            {"id": "54J", "name": "DeFuniak Springs Airport (54J)"},
            {"id": "DED", "name": "Dehradun (DED)"},
            {"id": "DEZ", "name": "Deir Zzor (DEZ)"},
            {"id": "PDK", "name": "Dekalb-Peachtree Airport (PDK)"},
            {"id": "PMV", "name": "Del Caribe Intl Gen Santiago Marino (PMV)"},
            {"id": "CEC", "name": "Del Norte County Airport (CEC)"},
            {"id": "NTR", "name": "Del Norte Intl (NTR)"},
            {"id": "DRT", "name": "Del Rio Intl (DRT)"},
            {"id": "MIE", "name": "Delaware County Airport (MIE)"},
            {"id": "YWJ", "name": "Deline (YWJ)"},
            {"id": "4U9", "name": "Dell Flight Strip (4U9)"},
            {"id": "ESC", "name": "Delta County Airport (ESC)"},
            {"id": "DTA", "name": "Delta Municipal Airport (DTA)"},
            {"id": "DEM", "name": "Dembidollo Airport (DEM)"},
            {"id": "DEN", "name": "Denver Intl (DEN)"},
            {"id": "CHR", "name": "Deols (CHR)"},
            {"id": "PGK", "name": "Depati Amir (PGK)"},
            {"id": "SSA", "name": "Deputado Luis Eduardo Magalhaes (SSA)"},
            {"id": "DEA", "name": "Dera Ghazi Khan Airport (DEA)"},
            {"id": "DSK", "name": "Dera Ismael Khan Airport (DSK)"},
            {"id": "DRB", "name": "Derby Airport (DRB)"},
            {"id": "DSM", "name": "Des Moines Intl (DSM)"},
            {"id": "M94", "name": "Desert Aire (M94)"},
            {"id": "DES", "name": "Desroches (DES)"},
            {"id": "DTS", "name": "Destin (DTS)"},
            {"id": "DTW", "name": "Detroit Metro Wayne Co (DTW)"},
            {"id": "ANR", "name": "Deurne (ANR)"},
            {"id": "IDR", "name": "Devi Ahilyabai Holkar (IDR)"},
            {"id": "DVL", "name": "Devils Lake Regional Airport (DVL)"},
            {"id": "DPO", "name": "Devonport Airport (DPO)"},
            {"id": "DBD", "name": "Dhanbad (DBD)"},
            {"id": "DHI", "name": "Dhangarhi (DHI)"},
            {"id": "DHG", "name": "Dhigurah Centara Grand Maldives (DHG)"},
            {"id": "DTI", "name": "Diamantina Airport (DTI)"},
            {"id": "DMT", "name": "Diamantino Airport (DMT)"},
            {"id": "MOH", "name": "Dibrugarh (MOH)"},
            {"id": "DIB", "name": "Dibrugarh Airport (DIB)"},
            {"id": "DIK", "name": "Dickinson Theodore Roosevelt Regional Airport (DIK)"},
            {"id": "DIW", "name": "Dickwella Airport (DIW)"},
            {"id": "IQQ", "name": "Diego Aracena Intl (IQQ)"},
            {"id": "FAJ", "name": "Diego Jimenez Torres (FAJ)"},
            {"id": "DIN", "name": "Dien Bien Phu Airport (DIN)"},
            {"id": "DKS", "name": "Dikson Airport (DKS)"},
            {"id": "EEN", "name": "Dillant Hopkins Airport (EEN)"},
            {"id": "DLG", "name": "Dillingham (DLG)"},
            {"id": "HDH", "name": "Dillingham (HDH)"},
            {"id": "DLY", "name": "Dillon's Bay Airport (DLY)"},
            {"id": "DMU", "name": "Dimapur Airport (DMU)"},
            {"id": "AXD", "name": "Dimokritos (AXD)"},
            {"id": "PTB", "name": "Dinwiddie County Airport (PTB)"},
            {"id": "ZTH", "name": "Dionysios Solomos (ZTH)"},
            {"id": "NIM", "name": "Diori Hamani (NIM)"},
            {"id": "CRK", "name": "Diosdado Macapagal International (CRK)"},
            {"id": "DPL", "name": "Dipolog (DPL)"},
            {"id": "DIG", "name": "Diqing Airport (DIG)"},
            {"id": "DIR", "name": "Dire Dawa Intl (DIR)"},
            {"id": "DIU", "name": "Diu Airport (DIU)"},
            {"id": "URC", "name": "Diwopu (URC)"},
            {"id": "MVF", "name": "Dix Sept Rosado Airport (MVF)"},
            {"id": "DIY", "name": "Diyarbakir (DIY)"},
            {"id": "DOE", "name": "Djoemoe Airstrip (DOE)"},
            {"id": "DNK", "name": "Dnipropetrovsk Intl (DNK)"},
            {"id": "MGE", "name": "Dobbins Arb (MGE)"},
            {"id": "DDC", "name": "Dodge City Regional Airport (DDC)"},
            {"id": "DOD", "name": "Dodoma (DOD)"},
            {"id": "XOZ", "name": "Doha Free Zone Airport (XOZ)"},
            {"id": "DOH", "name": "Doha Intl (DOH)"},
            {"id": "DOP", "name": "Dolpa (DOP)"},
            {"id": "MCU", "name": "Domerat (MCU)"},
            {"id": "DME", "name": "Domododevo (DME)"},
            {"id": "GDL", "name": "Don Miguel Hidalgo Y Costilla Intl (GDL)"},
            {"id": "DMK", "name": "Don Muang Intl (DMK)"},
            {"id": "ZQL", "name": "Donaueschingen Villingen (ZQL)"},
            {"id": "CFN", "name": "Donegal Airport (CFN)"},
            {"id": "DOK", "name": "Donetsk Intl (DOK)"},
            {"id": "VDH", "name": "Dong Hoi (VDH)"},
            {"id": "TBB", "name": "Dong Tac (TBB)"},
            {"id": "DOG", "name": "Dongola (DOG)"},
            {"id": "HLD", "name": "Dongshan (HLD)"},
            {"id": "DOY", "name": "Dongying Airport (DOY)"},
            {"id": "DMD", "name": "Doomadgee Airport (DMD)"},
            {"id": "SUE", "name": "Door County Cherryland Airport (SUE)"},
            {"id": "DTM", "name": "Dortmund (DTM)"},
            {"id": "XAX", "name": "Dorval Railway Station (XAX)"},
            {"id": "DHN", "name": "Dothan Rgnl (DHN)"},
            {"id": "DLA", "name": "Douala (DLA)"},
            {"id": "DGL", "name": "Douglas Municipal Airport (DGL)"},
            {"id": "DQH", "name": "Douglas Municipal Airport (DQH)"},
            {"id": "DOU", "name": "Dourados Airport (DOU)"},
            {"id": "DOV", "name": "Dover Afb (DOV)"},
            {"id": "C91", "name": "Dowagiac Municipal Airport (C91)"},
            {"id": "YZD", "name": "Downsview (YZD)"},
            {"id": "MKC", "name": "Downtown (MKC)"},
            {"id": "NAG", "name": "Dr Ambedkar Intl (NAG)"},
            {"id": "VLV", "name": "Dr Antonio Nicolas Briceno (VLV)"},
            {"id": "JBQ", "name": "Dr Joaquin Balaguer International Airport (JBQ)"},
            {"id": "PJC", "name": "Dr. Augusto Roberto Fuster International Airport (PJC)"},
            {"id": "DRK", "name": "Drake Bay Airport (DRK)"},
            {"id": "FYV", "name": "Drake Fld (FYV)"},
            {"id": "TPL", "name": "Draughon Miller Central Texas Rgnl (TPL)"},
            {"id": "DRS", "name": "Dresden (DRS)"},
            {"id": "DRJ", "name": "Drietabbetje Airstrip (DRJ)"},
            {"id": "DRM", "name": "Drummond Island Airport (DRM)"},
            {"id": "YHD", "name": "Dryden Rgnl (YHD)"},
            {"id": "DWC", "name": "Dubai Al Maktoum (DWC)"},
            {"id": "DXB", "name": "Dubai Intl (DXB)"},
            {"id": "DBO", "name": "Dubbo (DBO)"},
            {"id": "DUB", "name": "Dublin (DUB)"},
            {"id": "DUJ", "name": "DuBois Regional Airport (DUJ)"},
            {"id": "DBV", "name": "Dubrovnik (DBV)"},
            {"id": "DBQ", "name": "Dubuque Rgnl (DBQ)"},
            {"id": "DLH", "name": "Duluth Intl (DLH)"},
            {"id": "DGT", "name": "Dumaguete (DGT)"},
            {"id": "LUV", "name": "Dumatubun Airport (LUV)"},
            {"id": "DND", "name": "Dundee (DND)"},
            {"id": "DUE", "name": "Dundo Airport (DUE)"},
            {"id": "DUD", "name": "Dunedin (DUD)"},
            {"id": "DNH", "name": "Dunhuang Airport (DNH)"},
            {"id": "DKI", "name": "Dunk Island Airport (DKI)"},
            {"id": "PQC", "name": "Duong Dong Airport (PQC)"},
            {"id": "DPA", "name": "Dupage (DPA)"},
            {"id": "DGO", "name": "Durango Intl (DGO)"},
            {"id": "DRO", "name": "Durango La Plata Co (DRO)"},
            {"id": "DUR", "name": "Durban Intl (DUR)"},
            {"id": "MME", "name": "Durham Tees Valley Airport (MME)"},
            {"id": "DYU", "name": "Dushanbe (DYU)"},
            {"id": "DUS", "name": "Dusseldorf (DUS)"},
            {"id": "QDU", "name": "Dusseldorf Hauptbahnhof (QDU)"},
            {"id": "QFO", "name": "Duxford Aerodrome (QFO)"},
            {"id": "ABZ", "name": "Dyce (ABZ)"},
            {"id": "DYS", "name": "Dyess Afb (DYS)"},
            {"id": "DZA", "name": "Dzaoudzi Pamandzi (DZA)"},
            {"id": "SVD", "name": "E T Joshua (SVD)"},
            {"id": "EAA", "name": "Eagle Airport (EAA)"},
            {"id": "EGE", "name": "Eagle Co Rgnl (EGE)"},
            {"id": "EGA", "name": "Eagle County Airport (EGA)"},
            {"id": "EGV", "name": "Eagle River (EGV)"},
            {"id": "W13", "name": "Eagle's Nest Airport (W13)"},
            {"id": "SYA", "name": "Eareckson As (SYA)"},
            {"id": "TSS", "name": "East 34th Street Heliport (TSS)"},
            {"id": "ELS", "name": "East London (ELS)"},
            {"id": "GGG", "name": "East Texas Rgnl (GGG)"},
            {"id": "57C", "name": "East Troy Municipal Airport (57C)"},
            {"id": "PDT", "name": "Eastern Oregon Regional Airport (PDT)"},
            {"id": "IZG", "name": "Eastern Slopes Regional (IZG)"},
            {"id": "MRB", "name": "Eastern WV Regional Airport (MRB)"},
            {"id": "CLL", "name": "Easterwood Fld (CLL)"},
            {"id": "ZEM", "name": "Eastmain River Airport (ZEM)"},
            {"id": "ESN", "name": "Easton-Newnam Field Airport (ESN)"},
            {"id": "EPM", "name": "Eastport Municipal Airport (EPM)"},
            {"id": "EBE", "name": "Ebenhofen BF (EBE)"},
            {"id": "QAS", "name": "Ech Cheliff (QAS)"},
            {"id": "EOI", "name": "Eday Airport (EOI)"},
            {"id": "EDI", "name": "Edinburgh (EDI)"},
            {"id": "ZXE", "name": "Edinburgh Waverly Station (ZXE)"},
            {"id": "YXD", "name": "Edmonton City Centre (YXD)"},
            {"id": "YEG", "name": "Edmonton Intl (YEG)"},
            {"id": "YET", "name": "Edson (YET)"},
            {"id": "SVI", "name": "Eduardo Falla Solano (SVI)"},
            {"id": "MAO", "name": "Eduardo Gomes Intl (MAO)"},
            {"id": "LYB", "name": "Edward Bodden Airfield (LYB)"},
            {"id": "MPV", "name": "Edward F Knapp State (MPV)"},
            {"id": "GAL", "name": "Edward G Pitka Sr (GAL)"},
            {"id": "EDW", "name": "Edwards Afb (EDW)"},
            {"id": "EEK", "name": "Eek Airport (EEK)"},
            {"id": "GRQ", "name": "Eelde (GRQ)"},
            {"id": "1H2", "name": "Effingham Memorial Airport (1H2)"},
            {"id": "HGA", "name": "Egal Intl (HGA)"},
            {"id": "EGX", "name": "Egegik Airport (EGX)"},
            {"id": "QEF", "name": "Egelsbach (QEF)"},
            {"id": "EGS", "name": "Egilsstadir (EGS)"},
            {"id": "VPS", "name": "Eglin Afb (VPS)"},
            {"id": "EIL", "name": "Eielson Afb (EIL)"},
            {"id": "ETH", "name": "Eilat (ETH)"},
            {"id": "EIN", "name": "Eindhoven (EIN)"},
            {"id": "YOA", "name": "Ekati (YOA)"},
            {"id": "KEK", "name": "Ekwok Airport (KEK)"},
            {"id": "EYP", "name": "El Alcaraván Airport (EYP)"},
            {"id": "LPB", "name": "El Alto Intl (LPB)"},
            {"id": "AAC", "name": "El Arish International Airport (AAC)"},
            {"id": "NDR", "name": "El Aroui Airport (NDR)"},
            {"id": "EBG", "name": "El Bagre Airport (EBG)"},
            {"id": "EHL", "name": "El Bolson (EHL)"},
            {"id": "EBM", "name": "El Borma (EBM)"},
            {"id": "FTE", "name": "El Calafate (FTE)"},
            {"id": "UIB", "name": "El Carano (UIB)"},
            {"id": "NJK", "name": "El Centro Naf (NJK)"},
            {"id": "ELX", "name": "El Chalten (ELX)"},
            {"id": "AXM", "name": "El Eden (AXM)"},
            {"id": "PVA", "name": "El Embrujo (PVA)"},
            {"id": "ELF", "name": "El Fashir (ELF)"},
            {"id": "ELG", "name": "El Golea (ELG)"},
            {"id": "EGR", "name": "El Gora (EGR)"},
            {"id": "MDO", "name": "El Jaguel / Punta del Este Airport (MDO)"},
            {"id": "CJC", "name": "El Loa (CJC)"},
            {"id": "ENI", "name": "El Nido Airport (ENI)"},
            {"id": "EBD", "name": "El Obeid (EBD)"},
            {"id": "ELP", "name": "El Paso Intl (ELP)"},
            {"id": "MDZ", "name": "El Plumerillo (MDZ)"},
            {"id": "PVE", "name": "El Porvenir (PVE)"},
            {"id": "ELE", "name": "EL Real Airport (ELE)"},
            {"id": "SAL", "name": "El Salvador Intl (SAL)"},
            {"id": "KOE", "name": "El Tari (KOE)"},
            {"id": "PMY", "name": "El Tehuelche (PMY)"},
            {"id": "PMC", "name": "El Tepual Intl (PMC)"},
            {"id": "ELT", "name": "El Tor (ELT)"},
            {"id": "NZJ", "name": "El Toro (NZJ)"},
            {"id": "EZS", "name": "Elazig (EZS)"},
            {"id": "ELC", "name": "Elcho Island Airport (ELC)"},
            {"id": "BOG", "name": "Eldorado Intl (BOG)"},
            {"id": "EDL", "name": "Eldoret Intl (EDL)"},
            {"id": "ATH", "name": "Eleftherios Venizelos Intl (ATH)"},
            {"id": "EAL", "name": "Elenak Airport (EAL)"},
            {"id": "ELV", "name": "Elfin Cove Seaplane Base (ELV)"},
            {"id": "ELI", "name": "Elim Airport (ELI)"},
            {"id": "ESL", "name": "Elista Airport (ESL)"},
            {"id": "ECG", "name": "Elizabeth City Cgas Rgnl (ECG)"},
            {"id": "0A9", "name": "Elizabethton Municipal Airport (0A9)"},
            {"id": "EKI", "name": "Elkhart Municipal (EKI)"},
            {"id": "EKN", "name": "Elkins Randolph Co Jennings Randolph (EKN)"},
            {"id": "EKO", "name": "Elko Regional Airport (EKO)"},
            {"id": "EFD", "name": "Ellington Fld (EFD)"},
            {"id": "ELL", "name": "Ellisras (ELL)"},
            {"id": "RCA", "name": "Ellsworth Afb (RCA)"},
            {"id": "CAG", "name": "Elmas (CAG)"},
            {"id": "EDF", "name": "Elmendorf Afb (EDF)"},
            {"id": "ELM", "name": "Elmira Corning Rgnl (ELM)"},
            {"id": "MEC", "name": "Eloy Alfaro Intl (MEC)"},
            {"id": "ELY", "name": "Ely Airport (ELY)"},
            {"id": "LYU", "name": "Ely Municipal (LYU)"},
            {"id": "SBO", "name": "Emanuel Co (SBO)"},
            {"id": "EME", "name": "Emden (EME)"},
            {"id": "KJA", "name": "Emelyanovo (KJA)"},
            {"id": "EMD", "name": "Emerald (EMD)"},
            {"id": "EMK", "name": "Emmonak Airport (EMK)"},
            {"id": "EMP", "name": "Emporia Municipal Airport (EMP)"},
            {"id": "EJT", "name": "Enejit Airport (EJT)"},
            {"id": "NBE", "name": "Enfidha - Zine El Abidine Ben Ali International Airport (NBE)"},
            {"id": "ENT", "name": "Eniwetok Airport (ENT)"},
            {"id": "ENF", "name": "Enontekio (ENF)"},
            {"id": "ONX", "name": "Enrique Adolfo Jimenez Airport (ONX)"},
            {"id": "DAV", "name": "Enrique Malek Intl (DAV)"},
            {"id": "ESE", "name": "Ensenada (ESE)"},
            {"id": "ENH", "name": "Enshi Airport (ENH)"},
            {"id": "EBB", "name": "Entebbe Intl (EBB)"},
            {"id": "LVA", "name": "Entrammes (LVA)"},
            {"id": "SXB", "name": "Entzheim (SXB)"},
            {"id": "ENU", "name": "Enugu (ENU)"},
            {"id": "BII", "name": "Enyu Airfield (BII)"},
            {"id": "3D2", "name": "Ephraim-Gibraltar Airport (3D2)"},
            {"id": "OMA", "name": "Eppley Afld (OMA)"},
            {"id": "EBL", "name": "Erbil Intl (EBL)"},
            {"id": "ECN", "name": "Ercan International Airport (ECN)"},
            {"id": "ERM", "name": "Erechim Airport (ERM)"},
            {"id": "ERF", "name": "Erfurt (ERF)"},
            {"id": "MLX", "name": "Erhac (MLX)"},
            {"id": "ERI", "name": "Erie Intl Tom Ridge Fld (ERI)"},
            {"id": "PCW", "name": "Erie-Ottawa Regional Airport (PCW)"},
            {"id": "ASR", "name": "Erkilet (ASR)"},
            {"id": "PRC", "name": "Ernest A Love Fld (PRC)"},
            {"id": "BAQ", "name": "Ernesto Cortissoz (BAQ)"},
            {"id": "ERS", "name": "Eros Airport (ERS)"},
            {"id": "ERC", "name": "Erzincan (ERC)"},
            {"id": "ERZ", "name": "Erzurum (ERZ)"},
            {"id": "ORN", "name": "Es Senia (ORN)"},
            {"id": "EBJ", "name": "Esbjerg (EBJ)"},
            {"id": "MYC", "name": "Escuela Mariscal Sucre Airport (MYC)"},
            {"id": "ESB", "name": "Esenboga (ESB)"},
            {"id": "IFN", "name": "Esfahan Shahid Beheshti Intl (IFN)"},
            {"id": "ESK", "name": "Eskisehir (ESK)"},
            {"id": "ESF", "name": "Esler Rgnl (ESF)"},
            {"id": "EPR", "name": "Esperance Airport (EPR)"},
            {"id": "EQS", "name": "Esquel (EQS)"},
            {"id": "ESX", "name": "Essen HBF (ESX)"},
            {"id": "ESS", "name": "Essen Mulheim (ESS)"},
            {"id": "ZES", "name": "Essen Railway (ZES)"},
            {"id": "ENC", "name": "Essey (ENC)"},
            {"id": "YEN", "name": "Estevan (YEN)"},
            {"id": "ANK", "name": "Etimesgut (ANK)"},
            {"id": "MAZ", "name": "Eugenio Maria De Hostos (MAZ)"},
            {"id": "YEU", "name": "Eureka (YEU)"},
            {"id": "EAP", "name": "EuroAirport (EAP)"},
            {"id": "BSL", "name": "EuroAirport Basel-Mulhouse-Freiburg (BSL)"},
            {"id": "QQU", "name": "Euston Station (QQU)"},
            {"id": "EVW", "name": "Evanston-Uinta CO Burns Fld (EVW)"},
            {"id": "EVV", "name": "Evansville Regional (EVV)"},
            {"id": "EVE", "name": "Evenes (EVE)"},
            {"id": "X01", "name": "Everglades Airpark (X01)"},
            {"id": "EXI", "name": "Excursion Inlet Seaplane Base (EXI)"},
            {"id": "AVO", "name": "Executive (AVO)"},
            {"id": "ORL", "name": "Executive (ORL)"},
            {"id": "EXT", "name": "Exeter (EXT)"},
            {"id": "EXM", "name": "Exmouth Airport (EXM)"},
            {"id": "GGT", "name": "Exuma Intl (GGT)"},
            {"id": "EUX", "name": "F D Roosevelt (EUX)"},
            {"id": "PPT", "name": "Faa'a International (PPT)"},
            {"id": "MVP", "name": "Fabio Alberto Leon Bentley (MVP)"},
            {"id": "FGI", "name": "Fagali'i (FGI)"},
            {"id": "FIE", "name": "Fair Isle Airport (FIE)"},
            {"id": "FAI", "name": "Fairbanks Intl (FAI)"},
            {"id": "SKA", "name": "Fairchild Afb (SKA)"},
            {"id": "FDW", "name": "Fairfield County Airport (FDW)"},
            {"id": "FFD", "name": "Fairford (FFD)"},
            {"id": "YZS", "name": "Fairmont Hot Springs (YZS)"},
            {"id": "LYP", "name": "Faisalabad Intl (LYP)"},
            {"id": "FBD", "name": "Faizabad Airport (FBD)"},
            {"id": "FKQ", "name": "Fak Fak (FKQ)"},
            {"id": "FAV", "name": "Fakarava (FAV)"},
            {"id": "AOI", "name": "Falconara (AOI)"},
            {"id": "APW", "name": "Faleolo Intl (APW)"},
            {"id": "NFL", "name": "Fallon Nas (NFL)"},
            {"id": "INL", "name": "Falls Intl (INL)"},
            {"id": "KFP", "name": "False Pass Airport (KFP)"},
            {"id": "FNE", "name": "Fane Airport (FNE)"},
            {"id": "RVA", "name": "Farafangana (RVA)"},
            {"id": "FAA", "name": "Faranah (FAA)"},
            {"id": "ULQ", "name": "Farfan (ULQ)"},
            {"id": "FAB", "name": "Farnborough (FAB)"},
            {"id": "FAO", "name": "Faro (FAO)"},
            {"id": "ZFA", "name": "Faro (ZFA)"},
            {"id": "NOS", "name": "Fascene (NOS)"},
            {"id": "BKS", "name": "Fatmawati Soekarno (BKS)"},
            {"id": "FYT", "name": "Faya Largeau (FYT)"},
            {"id": "FAY", "name": "Fayetteville Regional Grannis Field (FAY)"},
            {"id": "FAF", "name": "Felker Aaf (FAF)"},
            {"id": "SFF", "name": "Felts Fld (SFF)"},
            {"id": "TTT", "name": "Fengnin (TTT)"},
            {"id": "FNU", "name": "Fenosu (FNU)"},
            {"id": "FRE", "name": "Fera/Maringe Airport (FRE)"},
            {"id": "FEG", "name": "Fergana Airport (FEG)"},
            {"id": "BUD", "name": "Ferihegy (BUD)"},
            {"id": "55J", "name": "Fernandina Beach Municipal Airport (55J)"},
            {"id": "FEN", "name": "Fernando De Noronha (FEN)"},
            {"id": "SIG", "name": "Fernando Luis Ribas Dominicci (SIG)"},
            {"id": "R49", "name": "Ferry County Airport (R49)"},
            {"id": "WFI", "name": "Fianarantsoa (WFI)"},
            {"id": "KZI", "name": "Filippos (KZI)"},
            {"id": "FDY", "name": "Findlay Airport (FDY)"},
            {"id": "0G7", "name": "Finger Lakes Regional Airport (0G7)"},
            {"id": "FLR", "name": "Firenze (FLR)"},
            {"id": "FFA", "name": "First Flight Airport (FFA)"},
            {"id": "FIT", "name": "Fitchburg Municipal Airport (FIT)"},
            {"id": "FTI", "name": "Fitiuta Airport (FTI)"},
            {"id": "FZG", "name": "Fitzgerald Municipal Airport (FZG)"},
            {"id": "FIZ", "name": "Fitzroy Crossing Airport (FIZ)"},
            {"id": "FCO", "name": "Fiumicino (FCO)"},
            {"id": "RIR", "name": "Flabob Airport (RIR)"},
            {"id": "XFL", "name": "Flagler County Airport (XFL)"},
            {"id": "FLG", "name": "Flagstaff Pulliam Airport (FLG)"},
            {"id": "BON", "name": "Flamingo (BON)"},
            {"id": "FLF", "name": "Flensburg Schaferhaus (FLF)"},
            {"id": "BGO", "name": "Flesland (BGO)"},
            {"id": "KFB", "name": "Fliegerhost  (KFB)"},
            {"id": "YFO", "name": "Flin Flon (YFO)"},
            {"id": "FLS", "name": "Flinders Island Airport (FLS)"},
            {"id": "6S2", "name": "Florence (6S2)"},
            {"id": "FLO", "name": "Florence Rgnl (FLO)"},
            {"id": "FLW", "name": "Flores (FLW)"},
            {"id": "MTH", "name": "Florida Keys Marathon Airport (MTH)"},
            {"id": "FRO", "name": "Floro (FRO)"},
            {"id": "GFL", "name": "Floyd Bennett Memorial Airport (GFL)"},
            {"id": "AAH", "name": "Flugplatz Merzbrueck (AAH)"},
            {"id": "FCM", "name": "Flying Cloud Airport (FCM)"},
            {"id": "KHT", "name": "FOB Salerno (KHT)"},
            {"id": "FLD", "name": "Fond Du Lac County Airport (FLD)"},
            {"id": "ZFD", "name": "Fond-Du-Lac Airport (ZFD)"},
            {"id": "BOR", "name": "Fontaine Airport (BOR)"},
            {"id": "FBA", "name": "Fonte Boa Airport (FBA)"},
            {"id": "MRN", "name": "Foothills Regional Airport (MRN)"},
            {"id": "FOE", "name": "Forbes Fld (FOE)"},
            {"id": "IMT", "name": "Ford Airport (IMT)"},
            {"id": "FDE", "name": "Forde Bringeland (FDE)"},
            {"id": "25D", "name": "Forest Lake Airport (25D)"},
            {"id": "FRL", "name": "Forli (FRL)"},
            {"id": "FMA", "name": "Formosa (FMA)"},
            {"id": "FBU", "name": "Fornebu (FBU)"},
            {"id": "CCM", "name": "Forquilhinha (CCM)"},
            {"id": "YFA", "name": "Fort Albany Airport (YFA)"},
            {"id": "FBR", "name": "Fort Bridger (FBR)"},
            {"id": "YPY", "name": "Fort Chipewyan (YPY)"},
            {"id": "FNL", "name": "Fort Collins Loveland Muni (FNL)"},
            {"id": "FOD", "name": "Fort Dodge Rgnl (FOD)"},
            {"id": "YAG", "name": "Fort Frances Municipal Airport (YAG)"},
            {"id": "YGH", "name": "Fort Good Hope (YGH)"},
            {"id": "YFH", "name": "Fort Hope Airport (YFH)"},
            {"id": "RBN", "name": "Fort Jefferson (RBN)"},
            {"id": "FXE", "name": "Fort Lauderdale Executive (FXE)"},
            {"id": "FLL", "name": "Fort Lauderdale Hollywood Intl (FLL)"},
            {"id": "YMM", "name": "Fort Mcmurray (YMM)"},
            {"id": "NML", "name": "Fort McMurray - Mildred Lake Airport (NML)"},
            {"id": "ZFM", "name": "Fort Mcpherson (ZFM)"},
            {"id": "YYE", "name": "Fort Nelson (YYE)"},
            {"id": "YFR", "name": "Fort Resolution (YFR)"},
            {"id": "YER", "name": "Fort Severn Airport (YER)"},
            {"id": "YFS", "name": "Fort Simpson (YFS)"},
            {"id": "YSM", "name": "Fort Smith (YSM)"},
            {"id": "FSM", "name": "Fort Smith Rgnl (FSM)"},
            {"id": "YXJ", "name": "Fort St John (YXJ)"},
            {"id": "YJM", "name": "Fort St. James - Perison Airport (YJM)"},
            {"id": "FST", "name": "Fort Stockton Pecos Co (FST)"},
            {"id": "FWA", "name": "Fort Wayne (FWA)"},
            {"id": "FWM", "name": "Fort William Heliport (FWM)"},
            {"id": "AFW", "name": "Fort Worth Alliance Airport (AFW)"},
            {"id": "FTW", "name": "Fort Worth Meacham Intl (FTW)"},
            {"id": "FYU", "name": "Fort Yukon (FYU)"},
            {"id": "1OH", "name": "Fortman Airport (1OH)"},
            {"id": "FUO", "name": "Foshan (FUO)"},
            {"id": "7A4", "name": "Foster Field (7A4)"},
            {"id": "FZI", "name": "Fostoria Metropolitan Airport (FZI)"},
            {"id": "FOA", "name": "Foula Airport (FOA)"},
            {"id": "FOM", "name": "Foumban Nkounja (FOM)"},
            {"id": "FMN", "name": "Four Corners Rgnl (FMN)"},
            {"id": "NVS", "name": "Fourchambault (NVS)"},
            {"id": "FGL", "name": "Fox Glacier Airstrip (FGL)"},
            {"id": "NVK", "name": "Framnes (NVK)"},
            {"id": "FRC", "name": "Franca Airport (FRC)"},
            {"id": "FOK", "name": "Francis S Gabreski (FOK)"},
            {"id": "DVO", "name": "Francisco Bangoy International (DVO)"},
            {"id": "SPN", "name": "Francisco C Ada Saipan Intl (SPN)"},
            {"id": "JDF", "name": "Francisco De Assis (JDF)"},
            {"id": "OCC", "name": "Francisco De Orellana (OCC)"},
            {"id": "FRW", "name": "Francistown (FRW)"},
            {"id": "HOG", "name": "Frank Pais Intl (HOG)"},
            {"id": "MLS", "name": "Frank Wiley Field (MLS)"},
            {"id": "HHN", "name": "Frankfurt Hahn (HHN)"},
            {"id": "FRA", "name": "Frankfurt Main (FRA)"},
            {"id": "ZFR", "name": "Frankfurt Oder Hbf (ZFR)"},
            {"id": "ZRB", "name": "Frankfurt-Main Hauptbahnhof (ZRB)"},
            {"id": "FKL", "name": "Franklin (FKL)"},
            {"id": "BIK", "name": "Frans Kaisiepo (BIK)"},
            {"id": "MUC", "name": "Franz Josef Strauss (MUC)"},
            {"id": "C16", "name": "Frasca Field (C16)"},
            {"id": "1C9", "name": "Frazier Lake Airpark (1C9)"},
            {"id": "FBG", "name": "Fredericksburg Amtrak Station (FBG)"},
            {"id": "YFC", "name": "Fredericton (YFC)"},
            {"id": "FNA", "name": "Freetown Lungi (FNA)"},
            {"id": "FRJ", "name": "Frejus Saint Raphael (FRJ)"},
            {"id": "RBK", "name": "French Valley Airport (RBK)"},
            {"id": "MZM", "name": "Frescaty (MZM)"},
            {"id": "ASD", "name": "Fresh Creek (ASD)"},
            {"id": "FAT", "name": "Fresno Yosemite Intl (FAT)"},
            {"id": "FIG", "name": "Fria (FIG)"},
            {"id": "FRD", "name": "Friday Harbor Airport (FRD)"},
            {"id": "FBS", "name": "Friday Harbor Seaplane Base (FBS)"},
            {"id": "SUN", "name": "Friedman Mem (SUN)"},
            {"id": "FDH", "name": "Friedrichshafen (FDH)"},
            {"id": "TBU", "name": "Fua Amotu Intl (TBU)"},
            {"id": "FUE", "name": "Fuerteventura (FUE)"},
            {"id": "FJR", "name": "Fujairah Intl (FJR)"},
            {"id": "FUJ", "name": "Fukue (FUJ)"},
            {"id": "FUK", "name": "Fukuoka (FUK)"},
            {"id": "FKS", "name": "Fukushima Airport (FKS)"},
            {"id": "FUL", "name": "Fullerton Municipal Airport (FUL)"},
            {"id": "FTY", "name": "Fulton County Airport Brown Field (FTY)"},
            {"id": "FUN", "name": "Funafuti International (FUN)"},
            {"id": "FNR", "name": "Funter Bay Seaplane Base (FNR)"},
            {"id": "L06", "name": "Furnace Creek (L06)"},
            {"id": "FEL", "name": "Furstenfeldbruck (FEL)"},
            {"id": "FUS", "name": "Fussen (FUS)"},
            {"id": "FUX", "name": "Fussen BF (FUX)"},
            {"id": "FTA", "name": "Futuna Airport (FTA)"},
            {"id": "FUG", "name": "Fuyang Airport (FUG)"},
            {"id": "GAE", "name": "Gabes (GAE)"},
            {"id": "GAF", "name": "Gafsa (GAF)"},
            {"id": "GNV", "name": "Gainesville Rgnl (GNV)"},
            {"id": "GLK", "name": "Galcaio Airport (GLK)"},
            {"id": "GIG", "name": "Galeao Antonio Carlos Jobim (GIG)"},
            {"id": "GQQ", "name": "Galion Municipal Airport (GQQ)"},
            {"id": "BZN", "name": "Gallatin Field (BZN)"},
            {"id": "GEV", "name": "Gallivare (GEV)"},
            {"id": "GUP", "name": "Gallup Muni (GUP)"},
            {"id": "10C", "name": "Galt Field Airport (10C)"},
            {"id": "GWY", "name": "Galway (GWY)"},
            {"id": "TOB", "name": "Gamal Abdel Nasser Airport (TOB)"},
            {"id": "GAX", "name": "Gamba (GAX)"},
            {"id": "GAM", "name": "Gambell Airport (GAM)"},
            {"id": "GMB", "name": "Gambella (GMB)"},
            {"id": "GAN", "name": "Gan Island Airport (GAN)"},
            {"id": "YQX", "name": "Gander Intl (YQX)"},
            {"id": "KAG", "name": "Gangneung (KAG)"},
            {"id": "KVD", "name": "Ganja Airport (KVD)"},
            {"id": "GXH", "name": "Gannan (GXH)"},
            {"id": "KOW", "name": "Ganzhou Airport (KOW)"},
            {"id": "GAQ", "name": "Gao (GAQ)"},
            {"id": "XMN", "name": "Gaoqi (XMN)"},
            {"id": "GHE", "name": "Garachine Airport (GHE)"},
            {"id": "SRX", "name": "Gardabya Airport (SRX)"},
            {"id": "GCK", "name": "Garden City Rgnl (GCK)"},
            {"id": "OSL", "name": "Gardermoen (OSL)"},
            {"id": "GEN", "name": "Gardermoen Airport (GEN)"},
            {"id": "XHP", "name": "Gare de LEst (XHP)"},
            {"id": "PLY", "name": "Gare de Lyon (PLY)"},
            {"id": "XWG", "name": "Gare de Strasbourg (XWG)"},
            {"id": "XPG", "name": "Gare du Nord (XPG)"},
            {"id": "XGB", "name": "Gare Montparnasse (XGB)"},
            {"id": "RIL", "name": "Garfield County Regional Airport (RIL)"},
            {"id": "GAS", "name": "Garissa (GAS)"},
            {"id": "8M8", "name": "Garland Airport (8M8)"},
            {"id": "UVA", "name": "Garner Field (UVA)"},
            {"id": "FNI", "name": "Garons (FNI)"},
            {"id": "GOU", "name": "Garoua (GOU)"},
            {"id": "GGR", "name": "Garowe - International (GGR)"},
            {"id": "GYY", "name": "Gary Chicago International Airport (GYY)"},
            {"id": "GMI", "name": "Gasmata Island Airport (GMI)"},
            {"id": "YGP", "name": "Gaspe (YGP)"},
            {"id": "ELQ", "name": "Gassim (ELQ)"},
            {"id": "YND", "name": "Gatineau (YND)"},
            {"id": "GTA", "name": "Gatokae Airport (GTA)"},
            {"id": "LGW", "name": "Gatwick (LGW)"},
            {"id": "ZGU", "name": "Gaua Island Airport (ZGU)"},
            {"id": "GVX", "name": "Gavle (GVX)"},
            {"id": "GAY", "name": "Gaya (GAY)"},
            {"id": "GAH", "name": "Gayndah (GAH)"},
            {"id": "GZP", "name": "Gazipasa Airport (GZP)"},
            {"id": "BDT", "name": "Gbadolite (BDT)"},
            {"id": "GBK", "name": "Gbangbatok Airport (GBK)"},
            {"id": "QYD", "name": "Gdynia (QYD)"},
            {"id": "GEX", "name": "Geelong Airport (GEX)"},
            {"id": "GKE", "name": "Geilenkirchen (GKE)"},
            {"id": "SKE", "name": "Geiteryggen (SKE)"},
            {"id": "GDZ", "name": "Gelendzik (GDZ)"},
            {"id": "GMA", "name": "Gemena (GMA)"},
            {"id": "EGN", "name": "Geneina Airport (EGN)"},
            {"id": "TIJ", "name": "General Abelardo L Rodriguez Intl (TIJ)"},
            {"id": "PBL", "name": "General Bartolome Salom Intl (PBL)"},
            {"id": "BOS", "name": "General Edward Lawrence Logan Intl (BOS)"},
            {"id": "TTG", "name": "General Enrique Mosconi Airport (TTG)"},
            {"id": "MLM", "name": "General Francisco J Mujica Intl (MLM)"},
            {"id": "TAM", "name": "General Francisco Javier Mina Intl (TAM)"},
            {"id": "VER", "name": "General Heriberto Jara Intl (VER)"},
            {"id": "HMO", "name": "General Ignacio P Garcia Intl (HMO)"},
            {"id": "BLA", "name": "General Jose Antonio Anzoategui Intl (BLA)"},
            {"id": "CUP", "name": "General Jose Francisco Bermudez (CUP)"},
            {"id": "GYM", "name": "General Jose Maria Yanez Intl (GYM)"},
            {"id": "ACA", "name": "General Juan N Alvarez Intl (ACA)"},
            {"id": "RVD", "name": "General leite de Castro Airport (RVD)"},
            {"id": "ZCL", "name": "General Leobardo C Ruiz Intl (ZCL)"},
            {"id": "REX", "name": "General Lucio Blanco Intl (REX)"},
            {"id": "CGU", "name": "General Manuel Carlos Piar (CGU)"},
            {"id": "PZO", "name": "General Manuel Carlos Piar (PZO)"},
            {"id": "LAP", "name": "General Manuel Marquez De Leon Intl (LAP)"},
            {"id": "MCH", "name": "General Manuel Serrano (MCH)"},
            {"id": "MTY", "name": "General Mariano Escobedo Intl (MTY)"},
            {"id": "CVJ", "name": "General Mariano Matamoros (CVJ)"},
            {"id": "MKE", "name": "General Mitchell Intl (MKE)"},
            {"id": "CVM", "name": "General Pedro Jose Mendez Intl (CVM)"},
            {"id": "CUU", "name": "General R Fierro Villalobos Intl (CUU)"},
            {"id": "MZT", "name": "General Rafael Buelna Intl (MZT)"},
            {"id": "ESM", "name": "General Rivadeneira Airport (ESM)"},
            {"id": "MXL", "name": "General Rodolfo Sanchez Taboada Intl (MXL)"},
            {"id": "GES", "name": "General Santos International Airport (GES)"},
            {"id": "MAM", "name": "General Servando Canales Intl (MAM)"},
            {"id": "SNC", "name": "General Ulpiano Paez (SNC)"},
            {"id": "PRA", "name": "General Urquiza (PRA)"},
            {"id": "GVQ", "name": "Genesee County Airport (GVQ)"},
            {"id": "GVA", "name": "Geneve Cointrin (GVA)"},
            {"id": "GOA", "name": "Genova Sestri (GOA)"},
            {"id": "GRJ", "name": "George (GRJ)"},
            {"id": "IAH", "name": "George Bush Intercontinental (IAH)"},
            {"id": "SLU", "name": "George F L Charles (SLU)"},
            {"id": "GGE", "name": "Georgetown County Airport (GGE)"},
            {"id": "GTU", "name": "Georgetown Municipal Airport (GTU)"},
            {"id": "GRR", "name": "Gerald R Ford Intl (GRR)"},
            {"id": "GET", "name": "Geraldton Airport (GET)"},
            {"id": "YGQ", "name": "Geraldton Greenstone Regional (YGQ)"},
            {"id": "BUN", "name": "Gerardo Tobar Lopez (BUN)"},
            {"id": "7D9", "name": "Germack Airport (7D9)"},
            {"id": "CYB", "name": "Gerrard Smith Intl (CYB)"},
            {"id": "BGS", "name": "Gesundbrunnen (BGS)"},
            {"id": "LTD", "name": "Ghadames East (LTD)"},
            {"id": "GNZ", "name": "Ghanzi Airport (GNZ)"},
            {"id": "GHT", "name": "Ghat (GHT)"},
            {"id": "GSM", "name": "Gheshm Airport (GSM)"},
            {"id": "MUW", "name": "Ghriss (MUW)"},
            {"id": "GIB", "name": "Gibraltar (GIB)"},
            {"id": "GHF", "name": "Giebelstadt Aaf (GHF)"},
            {"id": "E63", "name": "Gila Bend Municipal Airport (E63)"},
            {"id": "GIF", "name": "Gilbert Airport (GIF)"},
            {"id": "CFX", "name": "Gilberto Lavaque (CFX)"},
            {"id": "GIL", "name": "Gilgit (GIL)"},
            {"id": "YGX", "name": "Gillam Airport (YGX)"},
            {"id": "SEE", "name": "Gillespie (SEE)"},
            {"id": "GCC", "name": "Gillette-Campbell County Airport (GCC)"},
            {"id": "49A", "name": "Gilmer County Airport (49A)"},
            {"id": "PUS", "name": "Gimhae Intl (PUS)"},
            {"id": "YGM", "name": "Gimli Industrial Park Airport (YGM)"},
            {"id": "GMP", "name": "Gimpo (GMP)"},
            {"id": "SEL", "name": "Gimpo International Airpot (SEL)"},
            {"id": "FOG", "name": "Gino Lisa (FOG)"},
            {"id": "GRO", "name": "Girona (GRO)"},
            {"id": "PNP", "name": "Girua Airport (PNP)"},
            {"id": "GIS", "name": "Gisborne (GIS)"},
            {"id": "GYI", "name": "Gisenyi (GYI)"},
            {"id": "YHK", "name": "Gjoa Haven (YHK)"},
            {"id": "GJR", "name": "Gjogur Airport (GJR)"},
            {"id": "FCA", "name": "Glacier Park Intl (FCA)"},
            {"id": "GLT", "name": "Gladstone Airport (GLT)"},
            {"id": "GDW", "name": "Gladwin Zettel Memorial Airport (GDW)"},
            {"id": "GLA", "name": "Glasgow (GLA)"},
            {"id": "GLI", "name": "Glen Innes (GLI)"},
            {"id": "GEU", "name": "Glendale Municipal Airport (GEU)"},
            {"id": "MON", "name": "Glentanner (MON)"},
            {"id": "GLO", "name": "Gloucestershire (GLO)"},
            {"id": "LFW", "name": "Gnassingbe Eyadema Intl (LFW)"},
            {"id": "GOI", "name": "Goa (GOI)"},
            {"id": "VDM", "name": "Gobernador Castello (VDM)"},
            {"id": "GGS", "name": "Gobernador Gregores Airport (GGS)"},
            {"id": "GDE", "name": "Gode Airport (GDE)"},
            {"id": "FTK", "name": "Godman Aaf (FTK)"},
            {"id": "MPH", "name": "Godofredo P (MPH)"},
            {"id": "YGO", "name": "Gods Lake Narrows Airport (YGO)"},
            {"id": "ZGI", "name": "Gods River Airport (ZGI)"},
            {"id": "IWD", "name": "Gogebic Iron County Airport (IWD)"},
            {"id": "VIX", "name": "Goiabeiras (VIX)"},
            {"id": "OOL", "name": "Gold Coast (OOL)"},
            {"id": "YGE", "name": "Golden Airport (YGE)"},
            {"id": "GTR", "name": "Golden Triangle Regional Airport (GTR)"},
            {"id": "SZZ", "name": "Goleniow (SZZ)"},
            {"id": "GLF", "name": "Golfito (GLF)"},
            {"id": "TLU", "name": "Golfo de Morrosquillo Airport (TLU)"},
            {"id": "GOQ", "name": "Golmud Airport (GOQ)"},
            {"id": "LCE", "name": "Goloson Intl (LCE)"},
            {"id": "GLV", "name": "Golovin Airport (GLV)"},
            {"id": "GOM", "name": "Goma (GOM)"},
            {"id": "GME", "name": "Gomel (GME)"},
            {"id": "GDQ", "name": "Gondar (GDQ)"},
            {"id": "GNU", "name": "Goodnews Airport (GNU)"},
            {"id": "YYR", "name": "Goose Bay (YYR)"},
            {"id": "GOP", "name": "Gorakhpur Airport (GOP)"},
            {"id": "GOR", "name": "Gore Airport (GOR)"},
            {"id": "YZE", "name": "Gore Bay Manitoulin (YZE)"},
            {"id": "GBT", "name": "Gorgan Airport (GBT)"},
            {"id": "GOZ", "name": "Gorna Oryahovitsa (GOZ)"},
            {"id": "RGK", "name": "Gorno-Altaysk Airport (RGK)"},
            {"id": "GKA", "name": "Goroka (GKA)"},
            {"id": "GML", "name": "Gostomel Antonov (GML)"},
            {"id": "GUL", "name": "Goulburn Airport (GUL)"},
            {"id": "GDA", "name": "Gounda Airport (GDA)"},
            {"id": "GOV", "name": "Gove Airport (GOV)"},
            {"id": "PVH", "name": "Governador Jorge Teixeira De Oliveira (PVH)"},
            {"id": "GVR", "name": "Governador Valadares Airport (GVR)"},
            {"id": "GHB", "name": "Governors Harbour (GHB)"},
            {"id": "KLF", "name": "Grabtsevo (KLF)"},
            {"id": "GRW", "name": "Graciosa (GRW)"},
            {"id": "GFN", "name": "Grafton Airport (GFN)"},
            {"id": "LPA", "name": "Gran Canaria (LPA)"},
            {"id": "LRV", "name": "Gran Roque Airport (LRV)"},
            {"id": "GRX", "name": "Granada (GRX)"},
            {"id": "FPO", "name": "Grand Bahama Intl (FPO)"},
            {"id": "JGC", "name": "Grand Canyon Heliport (JGC)"},
            {"id": "GCN", "name": "Grand Canyon National Park Airport (GCN)"},
            {"id": "1G4", "name": "Grand Canyon West Airport (1G4)"},
            {"id": "GCW", "name": "Grand Canyon West Airport (GCW)"},
            {"id": "SFG", "name": "Grand Case (SFG)"},
            {"id": "GCJ", "name": "Grand Central (GCJ)"},
            {"id": "RDR", "name": "Grand Forks Afb (RDR)"},
            {"id": "GFK", "name": "Grand Forks Intl (GFK)"},
            {"id": "C02", "name": "Grand Geneva Resort Airport (C02)"},
            {"id": "GJT", "name": "Grand Junction Regional (GJT)"},
            {"id": "GRM", "name": "Grand Marais Cook County Airport (GRM)"},
            {"id": "GPZ", "name": "Grand Rapids Itasca County (GPZ)"},
            {"id": "CRE", "name": "Grand Strand Airport (CRE)"},
            {"id": "YQU", "name": "Grande Prairie (YQU)"},
            {"id": "MWH", "name": "Grant Co Intl (MWH)"},
            {"id": "SVC", "name": "Grant County Airport (SVC)"},
            {"id": "BGI", "name": "Grantley Adams Intl (BGI)"},
            {"id": "GNT", "name": "Grants Milan Muni (GNT)"},
            {"id": "GFR", "name": "Granville (GFR)"},
            {"id": "GRF", "name": "Gray Aaf (GRF)"},
            {"id": "KGX", "name": "Grayling Airport (KGX)"},
            {"id": "GRZ", "name": "Graz (GRZ)"},
            {"id": "GBZ", "name": "Great Barrier Island (GBZ)"},
            {"id": "GBN", "name": "Great Bend Municipal (GBN)"},
            {"id": "GTF", "name": "Great Falls Intl (GTF)"},
            {"id": "GKL", "name": "Great Keppel Island (GKL)"},
            {"id": "BGM", "name": "Greater Binghamton Edwin A Link Fld (BGM)"},
            {"id": "CBE", "name": "Greater Cumberland Rgnl. (CBE)"},
            {"id": "IKK", "name": "Greater Kankakee (IKK)"},
            {"id": "YQM", "name": "Greater Moncton Intl (YQM)"},
            {"id": "ROC", "name": "Greater Rochester Intl (ROC)"},
            {"id": "LWB", "name": "Greenbrier Valley Airport (LWB)"},
            {"id": "GSP", "name": "Greenville-Spartanburg International (GSP)"},
            {"id": "YZX", "name": "Greenwood (YZX)"},
            {"id": "GWO", "name": "Greenwood Leflore (GWO)"},
            {"id": "POP", "name": "Gregorio Luperon Intl (POP)"},
            {"id": "PBF", "name": "Grider Fld (PBF)"},
            {"id": "6A2", "name": "Griffin-Spalding County Airport (6A2)"},
            {"id": "SKY", "name": "Griffing Sandusky (SKY)"},
            {"id": "RME", "name": "Griffiss Afld (RME)"},
            {"id": "GFF", "name": "Griffith Airport (GFF)"},
            {"id": "YGZ", "name": "Grise Fiord Airport (YGZ)"},
            {"id": "GUS", "name": "Grissom Arb (GUS)"},
            {"id": "JGR", "name": "Groennedal Heliport (JGR)"},
            {"id": "GTE", "name": "Groote Eylandt Airport (GTE)"},
            {"id": "GFY", "name": "Grootfontein (GFY)"},
            {"id": "GRS", "name": "Grosseto (GRS)"},
            {"id": "GON", "name": "Groton New London (GON)"},
            {"id": "TAR", "name": "Grottaglie (TAR)"},
            {"id": "29D", "name": "Grove City Airport (29D)"},
            {"id": "GRV", "name": "Grozny Airport (GRV)"},
            {"id": "GRY", "name": "Grímsey Airport (GRY)"},
            {"id": "GHU", "name": "Gualeguaychu (GHU)"},
            {"id": "GUM", "name": "Guam Intl (GUM)"},
            {"id": "GJA", "name": "Guanaja (GJA)"},
            {"id": "BJX", "name": "Guanajuato Intl (BJX)"},
            {"id": "GNM", "name": "Guanambi Airport (GNM)"},
            {"id": "GUQ", "name": "Guanare (GUQ)"},
            {"id": "GYS", "name": "Guangyuan Airport (GYS)"},
            {"id": "GZS", "name": "Guangzhou South Railway Station (GZS)"},
            {"id": "REC", "name": "Guararapes Gilberto Freyre Intl (REC)"},
            {"id": "WPU", "name": "Guardiamarina Zanartu Airport (WPU)"},
            {"id": "GRU", "name": "Guarulhos Gov Andre Franco Montouro (GRU)"},
            {"id": "ELU", "name": "Guemar Airport (ELU)"},
            {"id": "GCI", "name": "Guernsey (GCI)"},
            {"id": "GUB", "name": "Guerrero Negro Airport (GUB)"},
            {"id": "GTI", "name": "Guettin MecklenburgVorpommern Germany (GTI)"},
            {"id": "PPN", "name": "Guillermo Leon Valencia (PPN)"},
            {"id": "BES", "name": "Guipavas (BES)"},
            {"id": "GUI", "name": "Guiria (GUI)"},
            {"id": "GPT", "name": "Gulfport-Biloxi (GPT)"},
            {"id": "GKN", "name": "Gulkana (GKN)"},
            {"id": "ULU", "name": "Gulu Airport (ULU)"},
            {"id": "VOG", "name": "Gumrak (VOG)"},
            {"id": "GUC", "name": "Gunnison - Crested Butte (GUC)"},
            {"id": "NGQ", "name": "Gunsa (NGQ)"},
            {"id": "URY", "name": "Guriat (URY)"},
            {"id": "GUR", "name": "Gurney Airport (GUR)"},
            {"id": "GRP", "name": "Gurupi Airport (GRP)"},
            {"id": "QUS", "name": "Gusau (QUS)"},
            {"id": "FLA", "name": "Gustavo Artunduaga Paredes (FLA)"},
            {"id": "BCA", "name": "Gustavo Rizo (BCA)"},
            {"id": "ADZ", "name": "Gustavo Rojas Pinilla (ADZ)"},
            {"id": "GST", "name": "Gustavus Airport (GST)"},
            {"id": "GUT", "name": "Gutersloh (GUT)"},
            {"id": "GYU", "name": "Guyuan (GYU)"},
            {"id": "GWD", "name": "Gwadar (GWD)"},
            {"id": "GWL", "name": "Gwalior (GWL)"},
            {"id": "KWJ", "name": "Gwangju (KWJ)"},
            {"id": "GWE", "name": "Gweru Thornhill (GWE)"},
            {"id": "LZU", "name": "Gwinnett County Airport-Briscoe Field (LZU)"},
            {"id": "LWN", "name": "Gyumri (LWN)"},
            {"id": "QGY", "name": "Győr-Pér International Airport (QGY)"},
            {"id": "TJQ", "name": "H As Hanandjoeddin (TJQ)"},
            {"id": "ENE", "name": "H Hasan Aroeboesman (ENE)"},
            {"id": "MIR", "name": "Habib Bourguiba Intl (MIR)"},
            {"id": "HAC", "name": "Hachijojima (HAC)"},
            {"id": "HAE", "name": "Haeju Airport (HAE)"},
            {"id": "HGR", "name": "Hagerstown Regional Richard A Henson Field (HGR)"},
            {"id": "HFS", "name": "Hagfors Airport (HFS)"},
            {"id": "HFA", "name": "Haifa (HFA)"},
            {"id": "HAS", "name": "Hail (HAS)"},
            {"id": "HNS", "name": "Haines Airport (HNS)"},
            {"id": "HKD", "name": "Hakodate (HKD)"},
            {"id": "YHZ", "name": "Halifax Intl (YHZ)"},
            {"id": "HLP", "name": "Halim Perdanakusuma International Airport (HLP)"},
            {"id": "YUX", "name": "Hall Beach (YUX)"},
            {"id": "KEV", "name": "Halli (KEV)"},
            {"id": "DUC", "name": "Halliburton Field Airport (DUC)"},
            {"id": "HCQ", "name": "Halls Creek Airport (HCQ)"},
            {"id": "HAD", "name": "Halmstad (HAD)"},
            {"id": "HDM", "name": "Hamadan Airport (HDM)"},
            {"id": "HAM", "name": "Hamburg (HAM)"},
            {"id": "XFW", "name": "Hamburg Finkenwerder (XFW)"},
            {"id": "ZMB", "name": "Hamburg Hbf (ZMB)"},
            {"id": "4G2", "name": "Hamburg Inc Airport (4G2)"},
            {"id": "HMI", "name": "Hami Airport (HMI)"},
            {"id": "HLZ", "name": "Hamilton (HLZ)"},
            {"id": "YHM", "name": "Hamilton (YHM)"},
            {"id": "HLT", "name": "Hamilton Airport (HLT)"},
            {"id": "HTI", "name": "Hamilton Island Airport (HTI)"},
            {"id": "HFT", "name": "Hammerfest Airport (HFT)"},
            {"id": "HNM", "name": "Hana (HNM)"},
            {"id": "HNA", "name": "Hanamaki (HNA)"},
            {"id": "ZNF", "name": "Hanau Aaf (ZNF)"},
            {"id": "BHB", "name": "Hancock County - Bar Harbor (BHB)"},
            {"id": "BTH", "name": "Hang Nadim (BTH)"},
            {"id": "HAQ", "name": "Hanimaadhoo Airport (HAQ)"},
            {"id": "HAJ", "name": "Hannover (HAJ)"},
            {"id": "ZVM", "name": "Hannover Messe-Heliport (ZVM)"},
            {"id": "HZG", "name": "Hanzhong Airport (HZG)"},
            {"id": "HOI", "name": "Hao (HOI)"},
            {"id": "QHR", "name": "Harar Meda Airport (QHR)"},
            {"id": "HRE", "name": "Harare Intl (HRE)"},
            {"id": "HDI", "name": "Hardwick Field Airport (HDI)"},
            {"id": "0W3", "name": "Harford County Airport (0W3)"},
            {"id": "PIM", "name": "Harris County Airport (PIM)"},
            {"id": "MDT", "name": "Harrisburg Intl (MDT)"},
            {"id": "CKB", "name": "Harrison Marion Regional Airport (CKB)"},
            {"id": "PHD", "name": "Harry Clever Field Airport (PHD)"},
            {"id": "HFD", "name": "Hartford Brainard (HFD)"},
            {"id": "ZRT", "name": "Hartford Union Station (ZRT)"},
            {"id": "VSF", "name": "Hartness State (VSF)"},
            {"id": "ATL", "name": "Hartsfield Jackson Atlanta Intl (ATL)"},
            {"id": "UPG", "name": "Hasanuddin (UPG)"},
            {"id": "EUN", "name": "Hassan I Airport (EUN)"},
            {"id": "HRM", "name": "Hassi R Mel (HRM)"},
            {"id": "HGS", "name": "Hastings Airport (HGS)"},
            {"id": "HAA", "name": "Hasvik (HAA)"},
            {"id": "HDY", "name": "Hat Yai Intl (HDY)"},
            {"id": "HTY", "name": "Hatay Airport (HTY)"},
            {"id": "HTF", "name": "Hatfield (HTF)"},
            {"id": "CUR", "name": "Hato (CUR)"},
            {"id": "HBG", "name": "Hattiesburg Bobby L. Chain Municipal Airport (HBG)"},
            {"id": "PIB", "name": "Hattiesburg Laurel Regional Airport (PIB)"},
            {"id": "BNJ", "name": "Hauptbahnhof (BNJ)"},
            {"id": "ZSB", "name": "Hauptbahnhof (ZSB)"},
            {"id": "HVR", "name": "Havre City Co (HVR)"},
            {"id": "YGV", "name": "Havre Saint-Pierre Airport (YGV)"},
            {"id": "CEG", "name": "Hawarden (CEG)"},
            {"id": "YHY", "name": "Hay River (YHY)"},
            {"id": "HIS", "name": "Hayman Island Airport (HIS)"},
            {"id": "HYS", "name": "Hays Regional Airport (HYS)"},
            {"id": "HWD", "name": "Hayward Executive Airport (HWD)"},
            {"id": "HZL", "name": "Hazleton Municipal (HZL)"},
            {"id": "HKB", "name": "Healy River Airport (HKB)"},
            {"id": "LHR", "name": "Heathrow (LHR)"},
            {"id": "HDG", "name": "Hebei Handan Airport (HDG)"},
            {"id": "36U", "name": "Heber City Municipal Airport (36U)"},
            {"id": "FAR", "name": "Hector International Airport (FAR)"},
            {"id": "BCV", "name": "Hector Silva Airstrip (BCV)"},
            {"id": "HEH", "name": "Heho (HEH)"},
            {"id": "HEI", "name": "Heide-Büsum Airport (HEI)"},
            {"id": "HDB", "name": "Heidelberg (HDB)"},
            {"id": "HEK", "name": "Heihe Airport (HEK)"},
            {"id": "OHE", "name": "Heilongjiang Mohe Airport (OHE)"},
            {"id": "HLN", "name": "Helena Rgnl (HLN)"},
            {"id": "HGL", "name": "Helgoland-Düne Airport (HGL)"},
            {"id": "JHE", "name": "Helsingborg Cruise Port (JHE)"},
            {"id": "HEM", "name": "Helsinki Malmi (HEM)"},
            {"id": "HEL", "name": "Helsinki Vantaa (HEL)"},
            {"id": "HMV", "name": "Hemavan Airport (HMV)"},
            {"id": "HSH", "name": "Henderson Executive Airport (HSH)"},
            {"id": "HCN", "name": "Hengchun Airport (HCN)"},
            {"id": "OTP", "name": "Henri Coanda (OTP)"},
            {"id": "STX", "name": "Henry E Rohlsen (STX)"},
            {"id": "FSI", "name": "Henry Post Aaf (FSI)"},
            {"id": "TMA", "name": "Henry Tift Myers Airport (TMA)"},
            {"id": "HEA", "name": "Herat (HEA)"},
            {"id": "FLN", "name": "Hercilio Luz (FLN)"},
            {"id": "HDF", "name": "Heringsdorf Airport (HDF)"},
            {"id": "VTU", "name": "Hermanos Ameijeiras (VTU)"},
            {"id": "PBC", "name": "Hermanos Serdan Intl (PBC)"},
            {"id": "HEN", "name": "Hernesaari Heliport (HEN)"},
            {"id": "CIJ", "name": "Heroes Del Acre (CIJ)"},
            {"id": "HRN", "name": "Heron Island (HRN)"},
            {"id": "HEX", "name": "Herrera International Airport (HEX)"},
            {"id": "HVB", "name": "Hervey Bay Airport (HVB)"},
            {"id": "UVF", "name": "Hewanorra Intl (UVF)"},
            {"id": "BAK", "name": "Heydar Aliyev (BAK)"},
            {"id": "GYD", "name": "Heydar Aliyev (GYD)"},
            {"id": "HKY", "name": "Hickory Rgnl (HKY)"},
            {"id": "VDE", "name": "Hierro (VDE)"},
            {"id": "YOJ", "name": "High Level (YOJ)"},
            {"id": "HIF", "name": "Hill Afb (HIF)"},
            {"id": "INJ", "name": "Hillsboro Muni (INJ)"},
            {"id": "ITO", "name": "Hilo Intl (ITO)"},
            {"id": "HHH", "name": "Hilton Head (HHH)"},
            {"id": "HXD", "name": "Hilton Head Airport (HXD)"},
            {"id": "IRU", "name": "Hilton Iru fushi (IRU)"},
            {"id": "QYI", "name": "Hilversum Railway Station (QYI)"},
            {"id": "HIJ", "name": "Hiroshima (HIJ)"},
            {"id": "HIW", "name": "Hiroshima-Nishi (HIW)"},
            {"id": "UT3", "name": "Hite Airport (UT3)"},
            {"id": "AUQ", "name": "Hiva Oa-Atuona Airport (AUQ)"},
            {"id": "HBA", "name": "Hobart (HBA)"},
            {"id": "HBR", "name": "Hobart Muni (HBR)"},
            {"id": "HOD", "name": "Hodeidah Intl (HOD)"},
            {"id": "HDS", "name": "Hoedspruit Afb (HDS)"},
            {"id": "HOQ", "name": "Hof Plauen (HOQ)"},
            {"id": "HOJ", "name": "Hohenems (HOJ)"},
            {"id": "HKK", "name": "Hokitika (HKK)"},
            {"id": "HOA", "name": "Hola (HOA)"},
            {"id": "XYJ", "name": "Holesovice (XYJ)"},
            {"id": "HYL", "name": "Hollis Seaplane Base (HYL)"},
            {"id": "HMN", "name": "Holloman Afb (HMN)"},
            {"id": "HCR", "name": "Holy Cross Airport (HCR)"},
            {"id": "HOM", "name": "Homer (HOM)"},
            {"id": "HST", "name": "Homestead Arb (HST)"},
            {"id": "HDO", "name": "Hondo Municipal Airport (HDO)"},
            {"id": "HKG", "name": "Hong Kong Intl (HKG)"},
            {"id": "SHA", "name": "Hongqiao Intl (SHA)"},
            {"id": "HIR", "name": "Honiara International (HIR)"},
            {"id": "BEQ", "name": "Honington (BEQ)"},
            {"id": "HNL", "name": "Honolulu Intl (HNL)"},
            {"id": "HLR", "name": "Hood Aaf (HLR)"},
            {"id": "HOK", "name": "Hooker Creek Airport (HOK)"},
            {"id": "HNH", "name": "Hoonah Airport (HNH)"},
            {"id": "HPB", "name": "Hooper Bay Airport (HPB)"},
            {"id": "YHO", "name": "Hopedale Airport (YHO)"},
            {"id": "HID", "name": "Horn Island Airport (HID)"},
            {"id": "HFN", "name": "Hornafjordur (HFN)"},
            {"id": "YHN", "name": "Hornepayne (YHN)"},
            {"id": "HOR", "name": "Horta (HOR)"},
            {"id": "HTN", "name": "Hotan (HTN)"},
            {"id": "ALG", "name": "Houari Boumediene (ALG)"},
            {"id": "CMX", "name": "Houghton County Memorial Airport (CMX)"},
            {"id": "HUL", "name": "Houlton Intl (HUL)"},
            {"id": "CMR", "name": "Houssen (CMR)"},
            {"id": "HOV", "name": "Hovden (HOV)"},
            {"id": "HOW", "name": "Howard (HOW)"},
            {"id": "KKN", "name": "Hoybuktmoen (KKN)"},
            {"id": "GNA", "name": "Hrodno Airport (GNA)"},
            {"id": "HHQ", "name": "Hua Hin (HHQ)"},
            {"id": "HUH", "name": "Huahine (HUH)"},
            {"id": "HIA", "name": "Huai An Lianshui Airport (HIA)"},
            {"id": "HUN", "name": "Hualien (HUN)"},
            {"id": "NOV", "name": "Huambo (NOV)"},
            {"id": "CSX", "name": "Huanghua (CSX)"},
            {"id": "HHA", "name": "Huanghua Intl (HHA)"},
            {"id": "HYN", "name": "Huangyan Luqiao Airport (HYN)"},
            {"id": "HBX", "name": "Hubli Airport (HBX)"},
            {"id": "HUV", "name": "Hudiksvall (HUV)"},
            {"id": "YHB", "name": "Hudson Bay (YHB)"},
            {"id": "HSK", "name": "Huesca-Pirineos Airport (HSK)"},
            {"id": "HGD", "name": "Hughenden Airport (HGD)"},
            {"id": "HUS", "name": "Hughes Airport (HUS)"},
            {"id": "HUZ", "name": "Huizhou (HUZ)"},
            {"id": "HLF", "name": "Hultsfred (HLF)"},
            {"id": "HUW", "name": "Humaita Airport (HUW)"},
            {"id": "HUY", "name": "Humberside (HUY)"},
            {"id": "HUE", "name": "Humera Airport (HUE)"},
            {"id": "SVN", "name": "Hunter Aaf (SVN)"},
            {"id": "HSV", "name": "Huntsville International Airport-Carl T Jones Field (HSV)"},
            {"id": "HRG", "name": "Hurghada Intl (HRG)"},
            {"id": "HRT", "name": "Hurlburt Fld (HRT)"},
            {"id": "HON", "name": "Huron Rgnl (HON)"},
            {"id": "YEE", "name": "Huronia (YEE)"},
            {"id": "HZK", "name": "Husavik (HZK)"},
            {"id": "BDO", "name": "Husein Sastranegara (BDO)"},
            {"id": "HSL", "name": "Huslia Airport (HSL)"},
            {"id": "HUT", "name": "Hutchinson Municipal Airport (HUT)"},
            {"id": "WKM", "name": "Hwange National Park (WKM)"},
            {"id": "IBR", "name": "Hyakuri (IBR)"},
            {"id": "HYG", "name": "Hydaburg Seaplane Base (HYG)"},
            {"id": "WHD", "name": "Hyder Seaplane Base (WHD)"},
            {"id": "HYD", "name": "Hyderabad (HYD)"},
            {"id": "HDD", "name": "Hyderabad Airport (HDD)"},
            {"id": "HYV", "name": "Hyvinkaa (HYV)"},
            {"id": "IAS", "name": "Iasi (IAS)"},
            {"id": "IBA", "name": "Ibadan (IBA)"},
            {"id": "IBZ", "name": "Ibiza (IBZ)"},
            {"id": "TNG", "name": "Ibn Batouta (TNG)"},
            {"id": "YVA", "name": "Iconi Airport (YVA)"},
            {"id": "ICY", "name": "Icy Bay Airport (ICY)"},
            {"id": "IDA", "name": "Idaho Falls Rgnl (IDA)"},
            {"id": "IDL", "name": "Idlewild Intl (IDL)"},
            {"id": "IAA", "name": "Igarka Airport (IAA)"},
            {"id": "IGD", "name": "Igdir (IGD)"},
            {"id": "QFI", "name": "Iginniarfik Heliport (QFI)"},
            {"id": "IGG", "name": "Igiugig Airport (IGG)"},
            {"id": "YGT", "name": "Igloolik Airport (YGT)"},
            {"id": "CMW", "name": "Ignacio Agramonte Intl (CMW)"},
            {"id": "BQS", "name": "Ignatyevo (BQS)"},
            {"id": "BDR", "name": "Igor I Sikorsky Mem (BDR)"},
            {"id": "IHU", "name": "Ihu Airport (IHU)"},
            {"id": "JIK", "name": "Ikaria (JIK)"},
            {"id": "IKI", "name": "Iki (IKI)"},
            {"id": "IIL", "name": "Ilam Airport (IIL)"},
            {"id": "PFR", "name": "Ilebo Airport (PFR)"},
            {"id": "YGR", "name": "Iles De La Madeleine (YGR)"},
            {"id": "ILF", "name": "Ilford Airport (ILF)"},
            {"id": "IOS", "name": "Ilheus (IOS)"},
            {"id": "ILI", "name": "Iliamna (ILI)"},
            {"id": "VYS", "name": "Illinois Valley Regional (VYS)"},
            {"id": "VVZ", "name": "Illizi Takhamalt (VVZ)"},
            {"id": "ILO", "name": "Iloilo (ILO)"},
            {"id": "ILR", "name": "Ilorin (ILR)"},
            {"id": "JAV", "name": "Ilulissat (JAV)"},
            {"id": "IKA", "name": "Imam Khomeini (IKA)"},
            {"id": "IMB", "name": "Imbaimadai Airport (IMB)"},
            {"id": "IMM", "name": "Immokalee  (IMM)"},
            {"id": "QOW", "name": "Imo Airport (QOW)"},
            {"id": "IPL", "name": "Imperial Co (IPL)"},
            {"id": "IMF", "name": "Imphal (IMF)"},
            {"id": "BXN", "name": "Imsik (BXN)"},
            {"id": "IAM", "name": "In Amenas (IAM)"},
            {"id": "INZ", "name": "In Salah (INZ)"},
            {"id": "ICN", "name": "Incheon Intl (ICN)"},
            {"id": "UTO", "name": "Indian Mountain Lrrs (UTO)"},
            {"id": "IND", "name": "Indianapolis Intl (IND)"},
            {"id": "UMP", "name": "Indianapolis Metropolitan Airport (UMP)"},
            {"id": "IBL", "name": "Indigo Bay Lodge Airport (IBL)"},
            {"id": "DEL", "name": "Indira Gandhi Intl (DEL)"},
            {"id": "IMI", "name": "Ine Airport (IMI)"},
            {"id": "AGA", "name": "Inezgane (AGA)"},
            {"id": "CPE", "name": "Ingeniero Alberto Acuna Ongay Intl (CPE)"},
            {"id": "IGB", "name": "Ingeniero Jacobacci (IGB)"},
            {"id": "PCA", "name": "Ingeniero Juan Guillermo Villasana (PCA)"},
            {"id": "IGS", "name": "Ingolstadt BF (IGS)"},
            {"id": "INH", "name": "Inhambane (INH)"},
            {"id": "INQ", "name": "Inisheer (INQ)"},
            {"id": "IIA", "name": "Inishmaan Aerodrome (IIA)"},
            {"id": "IOR", "name": "Inishmore Airport (IOR)"},
            {"id": "IFL", "name": "Innisfail (IFL)"},
            {"id": "INN", "name": "Innsbruck (INN)"},
            {"id": "INO", "name": "Inongo Airport (INO)"},
            {"id": "OCF", "name": "International Airport (OCF)"},
            {"id": "YPH", "name": "Inukjuak Airport (YPH)"},
            {"id": "YEV", "name": "Inuvik Mike Zubko (YEV)"},
            {"id": "IVC", "name": "Invercargill (IVC)"},
            {"id": "IVR", "name": "Inverell (IVR)"},
            {"id": "INV", "name": "Inverness (INV)"},
            {"id": "IYK", "name": "Inyokern Airport (IYK)"},
            {"id": "IOA", "name": "Ioannina (IOA)"},
            {"id": "CFU", "name": "Ioannis Kapodistrias Intl (CFU)"},
            {"id": "ECA", "name": "Iosco County (ECA)"},
            {"id": "IOW", "name": "Iowa City Municipal Airport (IOW)"},
            {"id": "IPA", "name": "Ipota Airport (IPA)"},
            {"id": "YFB", "name": "Iqaluit (YFB)"},
            {"id": "NOC", "name": "Ireland West Knock (NOC)"},
            {"id": "IRI", "name": "Iringa (IRI)"},
            {"id": "IKT", "name": "Irkutsk (IKT)"},
            {"id": "IFJ", "name": "Isafjordur (IFJ)"},
            {"id": "4A9", "name": "Isbell Field Airport (4A9)"},
            {"id": "ISG", "name": "Ishigaki (ISG)"},
            {"id": "IRD", "name": "Ishurdi (IRD)"},
            {"id": "PKN", "name": "Iskandar (PKN)"},
            {"id": "ISJ", "name": "Isla Mujeres (ISJ)"},
            {"id": "YIV", "name": "Island Lake Airport (YIV)"},
            {"id": "ILY", "name": "Islay (ILY)"},
            {"id": "IOM", "name": "Isle Of Man (IOM)"},
            {"id": "ISC", "name": "ISLES OF SCILLY (ISC)"},
            {"id": "PBP", "name": "Islita Airport (PBP)"},
            {"id": "ISE", "name": "Isparta Süleyman Demirel Airport (ISE)"},
            {"id": "ITH", "name": "Ithaca Tompkins Rgnl (ITH)"},
            {"id": "ITK", "name": "Itokama Airport (ITK)"},
            {"id": "OBY", "name": "Ittoqqortoormiit Heliport (OBY)"},
            {"id": "IVL", "name": "Ivalo (IVL)"},
            {"id": "IFO", "name": "Ivano Frankivsk International Airport (IFO)"},
            {"id": "TNR", "name": "Ivato (TNR)"},
            {"id": "YIK", "name": "Ivujivik Airport (YIK)"},
            {"id": "IWJ", "name": "Iwami Airport (IWJ)"},
            {"id": "IWO", "name": "Iwo Jima (IWO)"},
            {"id": "ZIH", "name": "Ixtapa Zihuatanejo Intl (ZIH)"},
            {"id": "IJK", "name": "Izhevsk Airport (IJK)"},
            {"id": "IZO", "name": "Izumo (IZO)"},
            {"id": "BQU", "name": "J F Mitchell Airport (BQU)"},
            {"id": "BUQ", "name": "J M Nkomo Intl (BUQ)"},
            {"id": "JLR", "name": "Jabalpur (JLR)"},
            {"id": "JAB", "name": "Jabiru (JAB)"},
            {"id": "JAT", "name": "Jabot Airport (JAT)"},
            {"id": "JKA", "name": "Jack Edwards Airport (JKA)"},
            {"id": "HHR", "name": "Jack Northrop Fld Hawthorne Muni (HHR)"},
            {"id": "19A", "name": "Jackson County Airport (19A)"},
            {"id": "JAN", "name": "Jackson Evers Intl (JAN)"},
            {"id": "JAC", "name": "Jackson Hole Airport (JAC)"},
            {"id": "JAX", "name": "Jacksonville Intl (JAX)"},
            {"id": "NIP", "name": "Jacksonville Nas (NIP)"},
            {"id": "TRM", "name": "Jacqueline Cochran Regional Airport (TRM)"},
            {"id": "JAQ", "name": "Jacquinot Bay Airport (JAQ)"},
            {"id": "GDT", "name": "JAGS McCartney International Airport (GDT)"},
            {"id": "CFG", "name": "Jaime Gonzalez (CFG)"},
            {"id": "JAI", "name": "Jaipur (JAI)"},
            {"id": "JSA", "name": "Jaisalmer (JSA)"},
            {"id": "JAA", "name": "Jalalabad (JAA)"},
            {"id": "GTO", "name": "Jalaluddin (GTO)"},
            {"id": "UIT", "name": "Jaluit Airport (UIT)"},
            {"id": "DAY", "name": "James M Cox Dayton Intl (DAY)"},
            {"id": "JMS", "name": "Jamestown Regional Airport (JMS)"},
            {"id": "IXJ", "name": "Jammu (IXJ)"},
            {"id": "JGA", "name": "Jamnagar (JGA)"},
            {"id": "IXW", "name": "Jamshedpur (IXW)"},
            {"id": "ZXB", "name": "Jan Mayensfield (ZXB)"},
            {"id": "RGT", "name": "Japura (RGT)"},
            {"id": "JQE", "name": "Jaqué Airport (JQE)"},
            {"id": "RZE", "name": "Jasionka (RZE)"},
            {"id": "JEF", "name": "Jefferson City Memorial Airport (JEF)"},
            {"id": "2G2", "name": "Jefferson County Airpark (2G2)"},
            {"id": "0S9", "name": "Jefferson County Intl (0S9)"},
            {"id": "TWD", "name": "Jefferson County Intl (TWD)"},
            {"id": "SOQ", "name": "Jefman (SOQ)"},
            {"id": "JEJ", "name": "Jeh Airport (JEJ)"},
            {"id": "CJU", "name": "Jeju Intl (CJU)"},
            {"id": "09J", "name": "Jekyll Island Airport (09J)"},
            {"id": "JEE", "name": "Jeremie Airport (JEE)"},
            {"id": "XRY", "name": "Jerez (XRY)"},
            {"id": "JER", "name": "Jersey (JER)"},
            {"id": "JSR", "name": "Jessore (JSR)"},
            {"id": "JES", "name": "Jesup-Wayne County Airport (JES)"},
            {"id": "AGU", "name": "Jesus Teran Intl (AGU)"},
            {"id": "JPR", "name": "Ji-Paraná Airport (JPR)"},
            {"id": "JGD", "name": "Jiagedaqi Airport (JGD)"},
            {"id": "JMU", "name": "Jiamusi Airport (JMU)"},
            {"id": "CKG", "name": "Jiangbei (CKG)"},
            {"id": "JGN", "name": "Jiayuguan Airport (JGN)"},
            {"id": "GJL", "name": "Jijel (GJL)"},
            {"id": "JIJ", "name": "Jijiga Airport (JIJ)"},
            {"id": "JIM", "name": "Jimma (JIM)"},
            {"id": "ACJ", "name": "Jimmy Carter Regional (ACJ)"},
            {"id": "TNA", "name": "Jinan (TNA)"},
            {"id": "JGS", "name": "Jing Gang Shan Airport (JGS)"},
            {"id": "JDZ", "name": "Jingdezhen Airport (JDZ)"},
            {"id": "KNC", "name": "Jinggangshan (KNC)"},
            {"id": "JHG", "name": "Jinghong (JHG)"},
            {"id": "JNG", "name": "Jining Airport  (JNG)"},
            {"id": "KHI", "name": "Jinnah Intl (KHI)"},
            {"id": "JNZ", "name": "Jinzhou Airport (JNZ)"},
            {"id": "JIU", "name": "Jiujiang Lushan Airport (JIU)"},
            {"id": "JZH", "name": "Jiuzhaigou Huanglong (JZH)"},
            {"id": "JIW", "name": "Jiwani Airport (JIW)"},
            {"id": "JXA", "name": "Jixi Airport (JXA)"},
            {"id": "JCB", "name": "Joacaba Airport (JCB)"},
            {"id": "JDH", "name": "Jodhpur (JDH)"},
            {"id": "JOE", "name": "Joensuu (JOE)"},
            {"id": "PBM", "name": "Johan A Pengel Intl (PBM)"},
            {"id": "JNB", "name": "Johannesburg Intl (JNB)"},
            {"id": "MNI", "name": "John A. Osborne Airport (MNI)"},
            {"id": "JFK", "name": "John F Kennedy Intl (JFK)"},
            {"id": "RAC", "name": "John H. Batten Airport (RAC)"},
            {"id": "JST", "name": "John Murtha Johnstown-Cambria County Airport (JST)"},
            {"id": "SNA", "name": "John Wayne Arpt Orange Co (SNA)"},
            {"id": "OJC", "name": "Johnson County Airport (OJC)"},
            {"id": "JON", "name": "Johnston Atoll (JON)"},
            {"id": "JOL", "name": "Jolo Airport (JOL)"},
            {"id": "NBO", "name": "Jomo Kenyatta International (NBO)"},
            {"id": "JMO", "name": "Jomsom (JMO)"},
            {"id": "JBR", "name": "Jonesboro Muni (JBR)"},
            {"id": "JKG", "name": "Jonkoping (JKG)"},
            {"id": "JLN", "name": "Joplin Rgnl (JLN)"},
            {"id": "LIM", "name": "Jorge Chavez Intl (LIM)"},
            {"id": "SJE", "name": "Jorge E Gonzalez Torres (SJE)"},
            {"id": "CBB", "name": "Jorge Wilsterman (CBB)"},
            {"id": "JRH", "name": "Jorhat (JRH)"},
            {"id": "RVR", "name": "Jose Aponte de la Torre Airport (RVR)"},
            {"id": "BSC", "name": "Jose Celestino Mutis (BSC)"},
            {"id": "JSM", "name": "Jose de San Martin Airport (JSM)"},
            {"id": "GYE", "name": "Jose Joaquin De Olmedo Intl (GYE)"},
            {"id": "CZE", "name": "Jose Leonardo Chirinos (CZE)"},
            {"id": "MDE", "name": "Jose Maria Cordova (MDE)"},
            {"id": "HAV", "name": "Jose Marti Intl (HAV)"},
            {"id": "LSP", "name": "Josefa Camejo (LSP)"},
            {"id": "GPI", "name": "Juan Casiano (GPI)"},
            {"id": "VRA", "name": "Juan Gualberto Gomez Intl (VRA)"},
            {"id": "CAQ", "name": "Juan H. White (CAQ)"},
            {"id": "RTB", "name": "Juan Manuel Galvez Intl (RTB)"},
            {"id": "VIG", "name": "Juan Pablo Pérez Alfonso Airport (VIG)"},
            {"id": "SJO", "name": "Juan Santamaria Intl (SJO)"},
            {"id": "SRE", "name": "Juana Azurduy De Padilla (SRE)"},
            {"id": "SAB", "name": "Juancho E. Yrausquin (SAB)"},
            {"id": "SUB", "name": "Juanda (SUB)"},
            {"id": "JJI", "name": "Juanjui (JJI)"},
            {"id": "JUB", "name": "Juba (JUB)"},
            {"id": "JUJ", "name": "Jujuy (JUJ)"},
            {"id": "JCK", "name": "Julia Creek Airport (JCK)"},
            {"id": "JUL", "name": "Juliaca (JUL)"},
            {"id": "PIN", "name": "Julio Belem Airport (PIN)"},
            {"id": "JUM", "name": "Jumla (JUM)"},
            {"id": "JNU", "name": "Juneau Intl (JNU)"},
            {"id": "TRK", "name": "Juwata (TRK)"},
            {"id": "JWA", "name": "Jwaneng (JWA)"},
            {"id": "JYV", "name": "Jyvaskyla (JYV)"},
            {"id": "KDM", "name": "Kaadedhdhoo (KDM)"},
            {"id": "KBT", "name": "Kaben Airport (KBT)"},
            {"id": "ABK", "name": "Kabri Dehar Airport (ABK)"},
            {"id": "KBL", "name": "Kabul Intl (KBL)"},
            {"id": "HTA", "name": "Kadala (HTA)"},
            {"id": "DNA", "name": "Kadena Ab (DNA)"},
            {"id": "KDO", "name": "Kadhdhoo Airport (KDO)"},
            {"id": "KAD", "name": "Kaduna (KAD)"},
            {"id": "KED", "name": "Kaedi (KED)"},
            {"id": "KGE", "name": "Kagau Island Airport (KGE)"},
            {"id": "KOJ", "name": "Kagoshima (KOJ)"},
            {"id": "KCM", "name": "Kahramanmaras Airport (KCM)"},
            {"id": "OGG", "name": "Kahului (OGG)"},
            {"id": "KAI", "name": "Kaieteur (KAI)"},
            {"id": "KIA", "name": "Kaieteur International Airport (KIA)"},
            {"id": "KBZ", "name": "Kaikoura (KBZ)"},
            {"id": "IXH", "name": "Kailashahar (IXH)"},
            {"id": "KNG", "name": "Kaimana (KNG)"},
            {"id": "KAT", "name": "Kaitaia (KAT)"},
            {"id": "KAJ", "name": "Kajaani (KAJ)"},
            {"id": "AFE", "name": "Kake Airport (AFE)"},
            {"id": "KAE", "name": "Kake Seaplane Base (KAE)"},
            {"id": "KLX", "name": "Kalamata (KLX)"},
            {"id": "AZO", "name": "Kalamazoo (AZO)"},
            {"id": "LUP", "name": "Kalaupapa Airport (LUP)"},
            {"id": "KMV", "name": "Kalay Airport (KMV)"},
            {"id": "KAX", "name": "Kalbarri Airport (KAX)"},
            {"id": "FMI", "name": "Kalemie (FMI)"},
            {"id": "KGI", "name": "Kalgoorlie Boulder (KGI)"},
            {"id": "KLO", "name": "Kalibo (KLO)"},
            {"id": "KFG", "name": "Kalkgurung Airport (KFG)"},
            {"id": "LLA", "name": "Kallax (LLA)"},
            {"id": "KLR", "name": "Kalmar (KLR)"},
            {"id": "KLK", "name": "Kalokol (KLK)"},
            {"id": "KLG", "name": "Kalskag Airport (KLG)"},
            {"id": "KAL", "name": "Kaltag Airport (KAL)"},
            {"id": "JKL", "name": "Kalymnos Island (JKL)"},
            {"id": "KAR", "name": "Kamarang Airport (KAR)"},
            {"id": "KME", "name": "Kamembe (KME)"},
            {"id": "KMN", "name": "Kamina Base (KMN)"},
            {"id": "KAC", "name": "Kamishly Airport (KAC)"},
            {"id": "YKA", "name": "Kamloops (YKA)"},
            {"id": "KUY", "name": "Kamusi Airport (KUY)"},
            {"id": "LLW", "name": "Kamuzu Intl (LLW)"},
            {"id": "KGA", "name": "Kananga (KGA)"},
            {"id": "KJI", "name": "Kanas Airport (KJI)"},
            {"id": "KDH", "name": "Kandahar (KDH)"},
            {"id": "IXY", "name": "Kandla (IXY)"},
            {"id": "KDR", "name": "Kandrian Airport (KDR)"},
            {"id": "NGF", "name": "Kaneohe Bay Mcaf (NGF)"},
            {"id": "KGT", "name": "Kangding Airport (KGT)"},
            {"id": "XGR", "name": "Kangiqsualujjuaq (Georges River) Airport (XGR)"},
            {"id": "YWB", "name": "Kangiqsujuaq - Wakeham Bay Airport (YWB)"},
            {"id": "YKG", "name": "Kangirsuk Airport (YKG)"},
            {"id": "DHM", "name": "Kangra Airport (DHM)"},
            {"id": "KNN", "name": "Kankan (KNN)"},
            {"id": "JAF", "name": "Kankesanturai (JAF)"},
            {"id": "KNU", "name": "Kanpur (KNU)"},
            {"id": "KIX", "name": "Kansai (KIX)"},
            {"id": "MCI", "name": "Kansas City Intl (MCI)"},
            {"id": "KHH", "name": "Kaohsiung Intl (KHH)"},
            {"id": "KLC", "name": "Kaolack (KLC)"},
            {"id": "NAV", "name": "Kapadokya (NAV)"},
            {"id": "JHM", "name": "Kapalua (JHM)"},
            {"id": "KNP", "name": "Kapanda Airport (KNP)"},
            {"id": "YYU", "name": "Kapuskasing (YYU)"},
            {"id": "KRY", "name": "Karamay Airport (KRY)"},
            {"id": "KDL", "name": "Kardla (KDL)"},
            {"id": "KAB", "name": "Kariba Intl (KAB)"},
            {"id": "KLV", "name": "Karlovy Vary (KLV)"},
            {"id": "KSK", "name": "Karlskoga (KSK)"},
            {"id": "KSD", "name": "Karlstad Airport (KSD)"},
            {"id": "HAU", "name": "Karmoy (HAU)"},
            {"id": "KGJ", "name": "Karonga (KGJ)"},
            {"id": "AOK", "name": "Karpathos (AOK)"},
            {"id": "KTA", "name": "Karratha (KTA)"},
            {"id": "KSY", "name": "Kars Airport (KSY)"},
            {"id": "KSQ", "name": "Karshi Khanabad Airport (KSQ)"},
            {"id": "KYK", "name": "Karuluk Airport (KYK)"},
            {"id": "KRB", "name": "Karumba Airport (KRB)"},
            {"id": "KRP", "name": "Karup (KRP)"},
            {"id": "ZKB", "name": "Kasaba Bay Airport (ZKB)"},
            {"id": "XKS", "name": "Kasabonika Airport (XKS)"},
            {"id": "BBK", "name": "Kasane (BBK)"},
            {"id": "ZKE", "name": "Kashechewan Airport (ZKE)"},
            {"id": "KHG", "name": "Kashi (KHG)"},
            {"id": "KUK", "name": "Kasigluk Airport (KUK)"},
            {"id": "PSJ", "name": "Kasiguncu (PSJ)"},
            {"id": "KGN", "name": "Kasongo Lunda (KGN)"},
            {"id": "KSJ", "name": "Kasos (KSJ)"},
            {"id": "KSL", "name": "Kassala (KSL)"},
            {"id": "KSF", "name": "Kassel Calden (KSF)"},
            {"id": "KZS", "name": "Kastelorizo (KZS)"},
            {"id": "CPH", "name": "Kastrup (CPH)"},
            {"id": "MPA", "name": "Katima Mulilo Airport (MPA)"},
            {"id": "EUA", "name": "Kaufana Airport (EUA)"},
            {"id": "KFX", "name": "Kaufbeuren BF (KFX)"},
            {"id": "KAU", "name": "Kauhava (KAU)"},
            {"id": "KKR", "name": "Kaukura (KKR)"},
            {"id": "KUN", "name": "Kaunas Intl (KUN)"},
            {"id": "KVG", "name": "Kavieng Airport (KVG)"},
            {"id": "KAW", "name": "Kawthoung Airport (KAW)"},
            {"id": "KYS", "name": "Kayes Dag Dag (KYS)"},
            {"id": "KZN", "name": "Kazan (KZN)"},
            {"id": "BWD", "name": "KBWD (BWD)"},
            {"id": "EAR", "name": "Kearney Municipal Airport (EAR)"},
            {"id": "KGG", "name": "Kedougou (KGG)"},
            {"id": "BIX", "name": "Keesler Afb (BIX)"},
            {"id": "KMP", "name": "Keetmanshoop (KMP)"},
            {"id": "KEW", "name": "Keewaywin (KEW)"},
            {"id": "EFL", "name": "Kefallinia (EFL)"},
            {"id": "KEF", "name": "Keflavik International Airport (KEF)"},
            {"id": "ZKG", "name": "Kegaska Airport (ZKG)"},
            {"id": "KEZ", "name": "Kelani River-Peliyagoda Waterdrome (KEZ)"},
            {"id": "YLW", "name": "Kelowna (YLW)"},
            {"id": "KLS", "name": "Kelso Longview (KLS)"},
            {"id": "KEJ", "name": "Kemerovo (KEJ)"},
            {"id": "KEM", "name": "Kemi Tornio (KEM)"},
            {"id": "KEX", "name": "Kempten HBF (KEX)"},
            {"id": "POT", "name": "Ken Jones (POT)"},
            {"id": "ENA", "name": "Kenai Muni (ENA)"},
            {"id": "TMB", "name": "Kendall Tamiami Executive (TMB)"},
            {"id": "KEN", "name": "Kenema Airport (KEN)"},
            {"id": "KET", "name": "Kengtung (KET)"},
            {"id": "NNA", "name": "Kenitra (NNA)"},
            {"id": "KEH", "name": "Kenmore Air Harbor Inc Seaplane Base (KEH)"},
            {"id": "LKE", "name": "Kenmore Air Harbor Seaplane Base (LKE)"},
            {"id": "YQK", "name": "Kenora (YQK)"},
            {"id": "ENW", "name": "Kenosha Regional Airport (ENW)"},
            {"id": "XCM", "name": "Kent (XCM)"},
            {"id": "1G3", "name": "Kent State Airport (1G3)"},
            {"id": "EOK", "name": "Keokuk Municipal Airport (EOK)"},
            {"id": "KJP", "name": "Kerama Airport (KJP)"},
            {"id": "KHC", "name": "Kerch Intl (KHC)"},
            {"id": "KMA", "name": "Kerema Airport (KMA)"},
            {"id": "KEY", "name": "Kericho (KEY)"},
            {"id": "KKE", "name": "Kerikeri (KKE)"},
            {"id": "KRV", "name": "Kerio Valley (KRV)"},
            {"id": "KER", "name": "Kerman (KER)"},
            {"id": "ERV", "name": "Kerrville Municipal Airport (ERV)"},
            {"id": "KIR", "name": "Kerry (KIR)"},
            {"id": "KTE", "name": "Kerteh (KTE)"},
            {"id": "IXK", "name": "Keshod (IXK)"},
            {"id": "WFB", "name": "Ketchikan harbor Seaplane Base (WFB)"},
            {"id": "KTN", "name": "Ketchikan Intl (KTN)"},
            {"id": "MEI", "name": "Key Field (MEI)"},
            {"id": "OCA", "name": "Key Largo (OCA)"},
            {"id": "EYW", "name": "Key West Intl (EYW)"},
            {"id": "NQX", "name": "Key West Nas (NQX)"},
            {"id": "HJR", "name": "Khajuraho (HJR)"},
            {"id": "KHM", "name": "Khamti (KHM)"},
            {"id": "HMA", "name": "Khanty Mansiysk Airport (HMA)"},
            {"id": "KRT", "name": "Khartoum (KRT)"},
            {"id": "KHS", "name": "Khasab (KHS)"},
            {"id": "HTG", "name": "Khatanga Airport (HTG)"},
            {"id": "HMJ", "name": "Khmeinitskiy (HMJ)"},
            {"id": "UUS", "name": "Khomutovo (UUS)"},
            {"id": "KKC", "name": "Khon Kaen (KKC)"},
            {"id": "KHD", "name": "Khoram Abad Airport (KHD)"},
            {"id": "HVD", "name": "Khovd Airport (HVD)"},
            {"id": "KGD", "name": "Khrabrovo (KGD)"},
            {"id": "LBD", "name": "Khudzhand Airport (LBD)"},
            {"id": "KDD", "name": "Khuzdar Airport (KDD)"},
            {"id": "KHW", "name": "Khwai River Lodge (KHW)"},
            {"id": "OXF", "name": "Kidlington (OXF)"},
            {"id": "KEL", "name": "Kiel Holtenau (KEL)"},
            {"id": "KIP", "name": "KIEV  INTERNATIONAL AIRPORT (KIP)"},
            {"id": "KFA", "name": "Kiffa (KFA)"},
            {"id": "KGL", "name": "Kigali Intl (KGL)"},
            {"id": "TKQ", "name": "Kigoma Airport (TKQ)"},
            {"id": "TNJ", "name": "Kijang (TNJ)"},
            {"id": "KKX", "name": "Kikai Airport (KKX)"},
            {"id": "KRI", "name": "Kikori Airport (KRI)"},
            {"id": "LDI", "name": "Kikwetu Airport (LDI)"},
            {"id": "KKW", "name": "Kikwit (KKW)"},
            {"id": "ILU", "name": "Kilaguni (ILU)"},
            {"id": "KIO", "name": "Kili Airport (KIO)"},
            {"id": "JRO", "name": "Kilimanjaro Intl (JRO)"},
            {"id": "HKN", "name": "Kimbe Airport (HKN)"},
            {"id": "KIM", "name": "Kimberley (KIM)"},
            {"id": "YLC", "name": "Kimmirut Airport (YLC)"},
            {"id": "YKY", "name": "Kindersley (YKY)"},
            {"id": "KND", "name": "Kindu (KND)"},
            {"id": "KIE", "name": "Kineshma (KIE)"},
            {"id": "DHA", "name": "King Abdulaziz Ab (DHA)"},
            {"id": "JED", "name": "King Abdulaziz Intl (JED)"},
            {"id": "GIZ", "name": "King Abdullah Bin Abdulaziz (GIZ)"},
            {"id": "KVC", "name": "King Cove Airport (KVC)"},
            {"id": "DMM", "name": "King Fahd Intl (DMM)"},
            {"id": "OMF", "name": "King Hussein (OMF)"},
            {"id": "KNS", "name": "King Island Airport (KNS)"},
            {"id": "RUH", "name": "King Khaled Intl (RUH)"},
            {"id": "HBT", "name": "King Khaled Military City (HBT)"},
            {"id": "AKN", "name": "King Salmon (AKN)"},
            {"id": "KIF", "name": "Kingfisher Lake Airport (KIF)"},
            {"id": "IGM", "name": "Kingman Airport (IGM)"},
            {"id": "KGC", "name": "Kingscote Airport (KGC)"},
            {"id": "YGK", "name": "Kingston (YGK)"},
            {"id": "XEG", "name": "Kingston Train Station (XEG)"},
            {"id": "NQI", "name": "Kingsville Nas (NQI)"},
            {"id": "ISO", "name": "Kinston Regional Jetport (ISO)"},
            {"id": "KPN", "name": "Kipnuk Airport (KPN)"},
            {"id": "YKX", "name": "Kirkland Lake (YKX)"},
            {"id": "IRK", "name": "Kirksville Regional Airport (IRK)"},
            {"id": "KIK", "name": "Kirkuk AB (KIK)"},
            {"id": "KOI", "name": "Kirkwall (KOI)"},
            {"id": "KGO", "name": "Kirovograd (KGO)"},
            {"id": "KVK", "name": "Kirovsk-Apatity Airport (KVK)"},
            {"id": "IKR", "name": "Kirtland Air Force Base (IKR)"},
            {"id": "KRN", "name": "Kiruna (KRN)"},
            {"id": "FKI", "name": "Kisangani Simisini (FKI)"},
            {"id": "KIH", "name": "Kish Island (KIH)"},
            {"id": "KMU", "name": "Kisimayu (KMU)"},
            {"id": "KSI", "name": "Kissidougou (KSI)"},
            {"id": "ISM", "name": "Kissimmee Gateway Airport (ISM)"},
            {"id": "KIS", "name": "Kisumu (KIS)"},
            {"id": "KTD", "name": "Kitadaito (KTD)"},
            {"id": "KTL", "name": "Kitale (KTL)"},
            {"id": "KIT", "name": "Kithira (KIT)"},
            {"id": "KKB", "name": "Kitoi Bay Seaplane Base (KKB)"},
            {"id": "KTT", "name": "Kittila (KTT)"},
            {"id": "UNG", "name": "Kiunga Airport (UNG)"},
            {"id": "KVL", "name": "Kivalina Airport (KVL)"},
            {"id": "KWY", "name": "Kiwayu (Mkononi) Airport (KWY)"},
            {"id": "MJF", "name": "Kjaerstad (MJF)"},
            {"id": "KRS", "name": "Kjevik (KRS)"},
            {"id": "LMT", "name": "Klamath Falls Airport (LMT)"},
            {"id": "KLW", "name": "Klawock Airport (KLW)"},
            {"id": "AQC", "name": "Klawock Seaplane Base (AQC)"},
            {"id": "KLZ", "name": "Kleinsee (KLZ)"},
            {"id": "VVO", "name": "Knevichi (VVO)"},
            {"id": "KNO", "name": "Knokke-Heist Westkapelle Heliport (KNO)"},
            {"id": "RKD", "name": "Knox County Regional Airport (RKD)"},
            {"id": "DKX", "name": "Knoxville Downtown Island Airport (DKX)"},
            {"id": "UKB", "name": "Kobe (UKB)"},
            {"id": "ZNV", "name": "Koblenz Winningen (ZNV)"},
            {"id": "OBU", "name": "Kobuk Airport (OBU)"},
            {"id": "KCZ", "name": "Kochi (KCZ)"},
            {"id": "ADQ", "name": "Kodiak (ADQ)"},
            {"id": "KGP", "name": "Kogalym International (KGP)"},
            {"id": "KCT", "name": "Koggala Airport (KCT)"},
            {"id": "KKD", "name": "Kokoda Airport (KKD)"},
            {"id": "KOV", "name": "Kokshetau Airport (KOV)"},
            {"id": "KLH", "name": "Kolhapur (KLH)"},
            {"id": "KGK", "name": "Koliganek Airport (KGK)"},
            {"id": "CGN", "name": "Koln Bonn (CGN)"},
            {"id": "KOX", "name": "Koln HBF (KOX)"},
            {"id": "SVX", "name": "Koltsovo (SVX)"},
            {"id": "KWZ", "name": "Kolwezi (KWZ)"},
            {"id": "KMQ", "name": "Komatsu (KMQ)"},
            {"id": "KXK", "name": "Komsomolsk-on-Amur Airport (KXK)"},
            {"id": "KOA", "name": "Kona Intl At Keahole (KOA)"},
            {"id": "UND", "name": "Konduz (UND)"},
            {"id": "KNQ", "name": "Kone (KNQ)"},
            {"id": "KKH", "name": "Kongiganak Airport (KKH)"},
            {"id": "KON", "name": "Kontum Airport (KON)"},
            {"id": "KYA", "name": "Konya (KYA)"},
            {"id": "GKK", "name": "Kooddoo (GKK)"},
            {"id": "KUT", "name": "Kopitnari (KUT)"},
            {"id": "HGO", "name": "Korhogo (HGO)"},
            {"id": "KRL", "name": "Korla Airport (KRL)"},
            {"id": "KXF", "name": "Koro Island Airport (KXF)"},
            {"id": "PCN", "name": "Koromiko (PCN)"},
            {"id": "KGS", "name": "Kos (KGS)"},
            {"id": "KSC", "name": "Kosice (KSC)"},
            {"id": "KSA", "name": "Kosrae (KSA)"},
            {"id": "KSN", "name": "Kostanay West Airport (KSN)"},
            {"id": "OSZ", "name": "Koszalin - Zegrze Pomorskie Airport (OSZ)"},
            {"id": "KTU", "name": "Kota (KTU)"},
            {"id": "ZWR", "name": "Kota Kinabalu Airport (ZWR)"},
            {"id": "BKI", "name": "Kota Kinabalu Intl (BKI)"},
            {"id": "KSZ", "name": "Kotlas Airport (KSZ)"},
            {"id": "KOT", "name": "Kotlik Airport (KOT)"},
            {"id": "ACC", "name": "Kotoka Intl (ACC)"},
            {"id": "KOU", "name": "Koulamoutou Airport (KOU)"},
            {"id": "KOC", "name": "Koumac (KOC)"},
            {"id": "KWM", "name": "Kowanyama Airport (KWM)"},
            {"id": "KKA", "name": "Koyuk Alfred Adams Airport (KKA)"},
            {"id": "KYU", "name": "Koyukuk Airport (KYU)"},
            {"id": "KBV", "name": "Krabi (KBV)"},
            {"id": "KRF", "name": "Kramfors Solleftea (KRF)"},
            {"id": "KTI", "name": "Kratie Airport (KTI)"},
            {"id": "PKV", "name": "Kresty (PKV)"},
            {"id": "KID", "name": "Kristianstad (KID)"},
            {"id": "VXO", "name": "Kronoberg (VXO)"},
            {"id": "MQP", "name": "Kruger Mpumalanga International Airport (MQP)"},
            {"id": "KOK", "name": "Kruunupyy (KOK)"},
            {"id": "NOI", "name": "Krymsk (NOI)"},
            {"id": "KUL", "name": "Kuala Lumpur Intl (KUL)"},
            {"id": "KUA", "name": "Kuantan (KUA)"},
            {"id": "KUG", "name": "Kubin Airport (KUG)"},
            {"id": "KCH", "name": "Kuching Intl (KCH)"},
            {"id": "KUD", "name": "Kudat Airport (KUD)"},
            {"id": "AKF", "name": "Kufra (AKF)"},
            {"id": "YBB", "name": "Kugaaruk (YBB)"},
            {"id": "YCO", "name": "Kugluktuk (YCO)"},
            {"id": "NTT", "name": "Kuini Lavenia Airport (NTT)"},
            {"id": "SVP", "name": "Kuito (SVP)"},
            {"id": "LKK", "name": "Kulik Lake Airport (LKK)"},
            {"id": "KUU", "name": "Kullu Manali (KUU)"},
            {"id": "TJU", "name": "Kulob Airport (TJU)"},
            {"id": "KUS", "name": "Kulusuk (KUS)"},
            {"id": "KMJ", "name": "Kumamoto (KMJ)"},
            {"id": "KMS", "name": "Kumasi Airport (KMS)"},
            {"id": "UEO", "name": "Kumejima (UEO)"},
            {"id": "NRK", "name": "Kungsangen (NRK)"},
            {"id": "KUV", "name": "Kunsan Air Base (KUV)"},
            {"id": "KNX", "name": "Kununurra (KNX)"},
            {"id": "KUO", "name": "Kuopio (KUO)"},
            {"id": "KCA", "name": "Kuqa Airport (KCA)"},
            {"id": "URE", "name": "Kuressaare (URE)"},
            {"id": "KRO", "name": "Kurgan Airport (KRO)"},
            {"id": "KUC", "name": "Kuria Airport (KUC)"},
            {"id": "URS", "name": "Kursk East Airport (URS)"},
            {"id": "KBY", "name": "Kurumoch (KBY)"},
            {"id": "KUF", "name": "Kurumoch (KUF)"},
            {"id": "KUH", "name": "Kushiro Airport (KUH)"},
            {"id": "YVP", "name": "Kuujjuaq (YVP)"},
            {"id": "YGW", "name": "Kuujjuarapik Airport (YGW)"},
            {"id": "KAO", "name": "Kuusamo (KAO)"},
            {"id": "KWI", "name": "Kuwait Intl (KWI)"},
            {"id": "KSU", "name": "Kvernberget (KSU)"},
            {"id": "KWT", "name": "Kwethluk Airport (KWT)"},
            {"id": "KWK", "name": "Kwigillingok Airport (KWK)"},
            {"id": "KYP", "name": "Kyaukpyu (KYP)"},
            {"id": "UKY", "name": "Kyoto (UKY)"},
            {"id": "KYZ", "name": "Kyzyl Airport (KYZ)"},
            {"id": "KZO", "name": "Kzyl-Orda (KZO)"},
            {"id": "LAQ", "name": "La Abraq Airport (LAQ)"},
            {"id": "GUA", "name": "La Aurora (GUA)"},
            {"id": "MAR", "name": "La Chinita Intl (MAR)"},
            {"id": "LCR", "name": "La Chorrera Airport (LCR)"},
            {"id": "LCL", "name": "La Coloma (LCL)"},
            {"id": "LSE", "name": "La Crosse Municipal (LSE)"},
            {"id": "JPU", "name": "La Defense Heliport (JPU)"},
            {"id": "DSD", "name": "La Désirade Airport (DSD)"},
            {"id": "LSC", "name": "La Florida (LSC)"},
            {"id": "TCO", "name": "La Florida (TCO)"},
            {"id": "LFR", "name": "La Fria (LFR)"},
            {"id": "AGF", "name": "La Garenne (AGF)"},
            {"id": "GMZ", "name": "La Gomera Airport (GMZ)"},
            {"id": "YGL", "name": "La Grande Riviere (YGL)"},
            {"id": "LGA", "name": "La Guardia (LGA)"},
            {"id": "GLJ", "name": "La Jagua Airport (GLJ)"},
            {"id": "LHX", "name": "La Junta Muni (LHX)"},
            {"id": "LMC", "name": "La Macarena (LMC)"},
            {"id": "XQP", "name": "La Managua (XQP)"},
            {"id": "SAP", "name": "La Mesa Intl (SAP)"},
            {"id": "LTT", "name": "La Môle Airport (LTT)"},
            {"id": "MZL", "name": "La Nubia (MZL)"},
            {"id": "SPC", "name": "La Palma (SPC)"},
            {"id": "LPD", "name": "La Pedrera Airport (LPD)"},
            {"id": "LPG", "name": "La Plata (LPG)"},
            {"id": "IRJ", "name": "La Rioja (IRJ)"},
            {"id": "BVE", "name": "La Roche (BVE)"},
            {"id": "LRH", "name": "La Rochelle-Ile de Re (LRH)"},
            {"id": "YVC", "name": "La Ronge (YVC)"},
            {"id": "ZLT", "name": "La Tabatière Airport (ZLT)"},
            {"id": "XAC", "name": "La Teste De Buch (XAC)"},
            {"id": "NOU", "name": "La Tontouta (NOU)"},
            {"id": "RLG", "name": "Laage (RLG)"},
            {"id": "LBS", "name": "Labasa Airport (LBS)"},
            {"id": "LEK", "name": "Labe (LEK)"},
            {"id": "LBR", "name": "Labrea Airport (LBR)"},
            {"id": "LBU", "name": "Labuan (LBU)"},
            {"id": "XLB", "name": "Lac Brochet Airport (XLB)"},
            {"id": "SKF", "name": "Lackland Afb Kelly Fld Annex (SKF)"},
            {"id": "FBK", "name": "Ladd Aaf (FBK)"},
            {"id": "LDO", "name": "Laduani Airstrip (LDO)"},
            {"id": "LYT", "name": "Lady Elliot Island (LYT)"},
            {"id": "LAY", "name": "Ladysmith (LAY)"},
            {"id": "LML", "name": "Lae Airport (LML)"},
            {"id": "LFT", "name": "Lafayette Rgnl (LFT)"},
            {"id": "LOO", "name": "Laghouat (LOO)"},
            {"id": "ING", "name": "Lago Argentino Airport (ING)"},
            {"id": "LGC", "name": "LaGrange-Callaway Airport (LGC)"},
            {"id": "CYR", "name": "Laguna de Los Patos International Airport (CYR)"},
            {"id": "LDU", "name": "Lahad Datu (LDU)"},
            {"id": "LHA", "name": "Lahr Airport (LHA)"},
            {"id": "YNT", "name": "Laishan (YNT)"},
            {"id": "TER", "name": "Lajes (TER)"},
            {"id": "LCH", "name": "Lake Charles Rgnl (LCH)"},
            {"id": "LCQ", "name": "Lake City Municipal Airport (LCQ)"},
            {"id": "SME", "name": "Lake Cumberland Regional Airport (SME)"},
            {"id": "LEL", "name": "Lake Evella Airport (LEL)"},
            {"id": "HII", "name": "Lake Havasu City Airport (HII)"},
            {"id": "LHD", "name": "Lake Hood Seaplane Base (LHD)"},
            {"id": "LKY", "name": "Lake Manyara (LKY)"},
            {"id": "LMY", "name": "Lake Murray Airport (LMY)"},
            {"id": "LKP", "name": "Lake Placid Airport (LKP)"},
            {"id": "YLS", "name": "Lake Simcoe (YLS)"},
            {"id": "TVL", "name": "Lake Tahoe Airport (TVL)"},
            {"id": "X07", "name": "Lake Wales Municipal Airport (X07)"},
            {"id": "LKB", "name": "Lakeba Island Airport (LKB)"},
            {"id": "NEW", "name": "Lakefront (NEW)"},
            {"id": "NEL", "name": "Lakehurst Naes (NEL)"},
            {"id": "ARV", "name": "Lakeland (ARV)"},
            {"id": "LAL", "name": "Lakeland Linder Regional Airport (LAL)"},
            {"id": "LLI", "name": "Lalibella (LLI)"},
            {"id": "LPM", "name": "Lamap Airport (LPM)"},
            {"id": "LAA", "name": "Lamar Muni (LAA)"},
            {"id": "LBQ", "name": "Lambarene (LBQ)"},
            {"id": "STL", "name": "Lambert St Louis Intl (STL)"},
            {"id": "LNB", "name": "Lamen Bay Airport (LNB)"},
            {"id": "SUF", "name": "Lamezia Terme (SUF)"},
            {"id": "LDN", "name": "Lamidanda (LDN)"},
            {"id": "LPT", "name": "Lampang (LPT)"},
            {"id": "LMP", "name": "Lampedusa (LMP)"},
            {"id": "LAU", "name": "Lamu Manda (LAU)"},
            {"id": "LNY", "name": "Lanai (LNY)"},
            {"id": "LNS", "name": "Lancaster Airport (LNS)"},
            {"id": "LEQ", "name": "Land's End / St. Just Airport (LEQ)"},
            {"id": "JLD", "name": "Landskrona (JLD)"},
            {"id": "GOT", "name": "Landvetter (GOT)"},
            {"id": "LGO", "name": "Langeoog Airport (LGO)"},
            {"id": "LGK", "name": "Langkawi Intl (LGK)"},
            {"id": "LFI", "name": "Langley Afb (LFI)"},
            {"id": "TOS", "name": "Langnes (TOS)"},
            {"id": "LLK", "name": "Lankaran International Airport (LLK)"},
            {"id": "LRT", "name": "Lann Bihoue (LRT)"},
            {"id": "LAI", "name": "Lannion (LAI)"},
            {"id": "04G", "name": "Lansdowne Airport (04G)"},
            {"id": "YLH", "name": "Lansdowne House Airport (YLH)"},
            {"id": "HLA", "name": "Lanseria (HLA)"},
            {"id": "IGQ", "name": "Lansing Municipal (IGQ)"},
            {"id": "KYD", "name": "Lanyu (KYD)"},
            {"id": "ACE", "name": "Lanzarote (ACE)"},
            {"id": "LHW", "name": "Lanzhou Airport (LHW)"},
            {"id": "LAO", "name": "Laoag Intl (LAO)"},
            {"id": "LPP", "name": "Lappeenranta (LPP)"},
            {"id": "LRR", "name": "Lar Airport (LRR)"},
            {"id": "LAR", "name": "Laramie Regional Airport (LAR)"},
            {"id": "LRD", "name": "Laredo Intl (LRD)"},
            {"id": "LRA", "name": "Larisa (LRA)"},
            {"id": "LCA", "name": "Larnaca (LCA)"},
            {"id": "KLN", "name": "Larsen Bay Airport (KLN)"},
            {"id": "SDQ", "name": "Las Americas Intl (SDQ)"},
            {"id": "CZU", "name": "Las Brujas (CZU)"},
            {"id": "LRU", "name": "Las Cruces Intl (LRU)"},
            {"id": "LHS", "name": "Las Heras Airport (LHS)"},
            {"id": "LVS", "name": "Las Vegas Muni (LVS)"},
            {"id": "QLA", "name": "Lasham (QLA)"},
            {"id": "LSH", "name": "Lashio (LSH)"},
            {"id": "QLT", "name": "Latina (QLT)"},
            {"id": "LTU", "name": "Latur Airport (LTU)"},
            {"id": "DLF", "name": "Laughlin Afb (DLF)"},
            {"id": "IFP", "name": "Laughlin-Bullhead Intl (IFP)"},
            {"id": "LST", "name": "Launceston (LST)"},
            {"id": "BED", "name": "Laurence G Hanscom Fld (BED)"},
            {"id": "CRU", "name": "Lauriston Airport (CRU)"},
            {"id": "JOI", "name": "Lauro Carneiro De Loyola (JOI)"},
            {"id": "PFB", "name": "Lauro Kurtz (PFB)"},
            {"id": "LVO", "name": "Laverton Airport (LVO)"},
            {"id": "LWY", "name": "Lawas Airport (LWY)"},
            {"id": "POZ", "name": "Lawica (POZ)"},
            {"id": "MWC", "name": "Lawrence J Timmerman Airport (MWC)"},
            {"id": "LWC", "name": "Lawrence Municipal (LWC)"},
            {"id": "LWM", "name": "Lawrence Municipal Airport (LWM)"},
            {"id": "LSF", "name": "Lawson Aaf (LSF)"},
            {"id": "LAW", "name": "Lawton-Fort Sill Regional Airport (LAW)"},
            {"id": "LAC", "name": "Layang Layang Airport (LAC)"},
            {"id": "LZC", "name": "Lazaro Cardenas (LZC)"},
            {"id": "LBG", "name": "Le Bourget (LBG)"},
            {"id": "CTT", "name": "Le Castellet (CTT)"},
            {"id": "FDF", "name": "Le Lamentin (FDF)"},
            {"id": "TLN", "name": "Le Palyvestre (TLN)"},
            {"id": "CET", "name": "Le Pontreau (CET)"},
            {"id": "PTP", "name": "Le Raizet (PTP)"},
            {"id": "LBI", "name": "Le Sequestre (LBI)"},
            {"id": "LTQ", "name": "Le Touquet Paris Plage (LTQ)"},
            {"id": "HOB", "name": "Lea Co Rgnl (HOB)"},
            {"id": "LEA", "name": "Learmonth (LEA)"},
            {"id": "LEB", "name": "Lebanon Municipal Airport (LEB)"},
            {"id": "S30", "name": "Lebanon State (S30)"},
            {"id": "LCC", "name": "Lecce (LCC)"},
            {"id": "GDN", "name": "Lech Walesa (GDN)"},
            {"id": "ANP", "name": "Lee Airport (ANP)"},
            {"id": "AIZ", "name": "Lee C Fine Memorial Airport (AIZ)"},
            {"id": "GVL", "name": "Lee Gilmer Memorial Airport (GVL)"},
            {"id": "LBA", "name": "Leeds Bradford (LBA)"},
            {"id": "JYO", "name": "Leesburg Executive Airport (JYO)"},
            {"id": "LWR", "name": "Leeuwarden (LWR)"},
            {"id": "LGP", "name": "Legazpi (LGP)"},
            {"id": "IXL", "name": "Leh (IXL)"},
            {"id": "ABE", "name": "Lehigh Valley Intl (ABE)"},
            {"id": "LER", "name": "Leinster Airport (LER)"},
            {"id": "LEJ", "name": "Leipzig Halle (LEJ)"},
            {"id": "VDB", "name": "Leirin (VDB)"},
            {"id": "RAO", "name": "Leite Lopes (RAO)"},
            {"id": "LKN", "name": "Leknes Airport (LKN)"},
            {"id": "LEY", "name": "Lelystad Airport (LEY)"},
            {"id": "SMN", "name": "Lemhi County Airport (SMN)"},
            {"id": "NLC", "name": "Lemoore Nas (NLC)"},
            {"id": "JAL", "name": "Lencero Airport (JAL)"},
            {"id": "ULK", "name": "Lensk (ULK)"},
            {"id": "LEN", "name": "Leon Airport (LEN)"},
            {"id": "LBV", "name": "Leon M Ba (LBV)"},
            {"id": "LNO", "name": "Leonora Airport (LNO)"},
            {"id": "DKR", "name": "Leopold Sedar Senghor Intl (DKR)"},
            {"id": "LRS", "name": "Leros (LRS)"},
            {"id": "LWK", "name": "Lerwick / Tingwall Airport (LWK)"},
            {"id": "EDM", "name": "Les Ajoncs (EDM)"},
            {"id": "GBJ", "name": "Les Bases Airport (GBJ)"},
            {"id": "LDG", "name": "Leshukonskoye Airport (LDG)"},
            {"id": "LIL", "name": "Lesquin (LIL)"},
            {"id": "YYZ", "name": "Lester B Pearson Intl (YYZ)"},
            {"id": "YQL", "name": "Lethbridge (YQL)"},
            {"id": "LTM", "name": "Lethem (LTM)"},
            {"id": "ADX", "name": "Leuchars (ADX)"},
            {"id": "LES", "name": "Leuterschach BF (LES)"},
            {"id": "CUF", "name": "Levaldigi (CUF)"},
            {"id": "KLL", "name": "Levelock Airport (KLL)"},
            {"id": "LEV", "name": "Levuka Airfield (LEV)"},
            {"id": "LOT", "name": "Lewis University Airport (LOT)"},
            {"id": "LEW", "name": "Lewiston Maine (LEW)"},
            {"id": "LWS", "name": "Lewiston Nez Perce Co (LWS)"},
            {"id": "LWT", "name": "Lewistown Municipal Airport (LWT)"},
            {"id": "LXA", "name": "Lhasa-Gonggar (LXA)"},
            {"id": "KWL", "name": "Liangjiang (KWL)"},
            {"id": "LYG", "name": "Lianyungang Airport (LYG)"},
            {"id": "LBL", "name": "Liberal Muni (LBL)"},
            {"id": "TLC", "name": "Licenciado Adolfo Lopez Mateos Intl (TLC)"},
            {"id": "MEX", "name": "Licenciado Benito Juarez Intl (MEX)"},
            {"id": "PVR", "name": "Licenciado Gustavo Diaz Ordaz Intl (PVR)"},
            {"id": "MID", "name": "Licenciado Manuel Crescencio Rejon Int (MID)"},
            {"id": "UPN", "name": "Licenciado Y Gen Ignacio Lopez Rayon (UPN)"},
            {"id": "VXC", "name": "Lichinga (VXC)"},
            {"id": "LDK", "name": "Lidkoping (LDK)"},
            {"id": "LGG", "name": "Liege (LGG)"},
            {"id": "XHN", "name": "Liege-Guillemins Railway Station (XHN)"},
            {"id": "LPX", "name": "Liepaja Intl (LPX)"},
            {"id": "LIF", "name": "Lifou (LIF)"},
            {"id": "HPA", "name": "Lifuka Island Airport (HPA)"},
            {"id": "LHG", "name": "Lightning Ridge Airport (LHG)"},
            {"id": "LIH", "name": "Lihue (LIH)"},
            {"id": "LJG", "name": "Lijiang Airport (LJG)"},
            {"id": "LIK", "name": "Likiep Airport (LIK)"},
            {"id": "LIX", "name": "Likoma Island Airport (LIX)"},
            {"id": "IXI", "name": "Lilabari (IXI)"},
            {"id": "XDB", "name": "Lille (XDB)"},
            {"id": "AOH", "name": "Lima Allen County Airport (AOH)"},
            {"id": "LMN", "name": "Limbang (LMN)"},
            {"id": "LXS", "name": "Limnos (LXS)"},
            {"id": "LIO", "name": "Limon Intl (LIO)"},
            {"id": "TLD", "name": "Limpopo Valley Airport (TLD)"},
            {"id": "LIN", "name": "Linate (LIN)"},
            {"id": "LNJ", "name": "Lincang Airport (LNJ)"},
            {"id": "LNK", "name": "Lincoln (LNK)"},
            {"id": "LHM", "name": "Lincoln Regional Airport Karl Harder Field (LHM)"},
            {"id": "LND", "name": "Lindau HBF (LND)"},
            {"id": "LDJ", "name": "Linden Airport (LDJ)"},
            {"id": "NF4", "name": "Lindsay (NF4)"},
            {"id": "LIP", "name": "Lins (LIP)"},
            {"id": "LNZ", "name": "Linz (LNZ)"},
            {"id": "LPK", "name": "Lipetsk Airport (LPK)"},
            {"id": "HZH", "name": "Liping Airport (HZH)"},
            {"id": "LIQ", "name": "Lisala (LIQ)"},
            {"id": "LIS", "name": "Lisboa (LIS)"},
            {"id": "NGB", "name": "Lishe (NGB)"},
            {"id": "LSY", "name": "Lismore Airport (LSY)"},
            {"id": "FAN", "name": "Lista (FAN)"},
            {"id": "ZGR", "name": "Little Grand Rapids Airport (ZGR)"},
            {"id": "LRF", "name": "Little Rock Afb (LRF)"},
            {"id": "TAO", "name": "Liuting (TAO)"},
            {"id": "LVK", "name": "Livermore Municipal (LVK)"},
            {"id": "LPL", "name": "Liverpool (LPL)"},
            {"id": "LIV", "name": "Livingood Airport (LIV)"},
            {"id": "LVI", "name": "Livingstone (LVI)"},
            {"id": "LZR", "name": "Lizard Island Airport (LZR)"},
            {"id": "LJU", "name": "Ljubljana (LJU)"},
            {"id": "ILD", "name": "Lleida-Alguaire Airport (ILD)"},
            {"id": "YLL", "name": "Lloydminster (YLL)"},
            {"id": "OLF", "name": "LM Clayton Airport (OLF)"},
            {"id": "ZJI", "name": "Locarno Airport (ZJI)"},
            {"id": "IRG", "name": "Lockhart River Airport (IRG)"},
            {"id": "LJA", "name": "Lodja Airport (LJA)"},
            {"id": "LOK", "name": "Lodwar (LOK)"},
            {"id": "LOE", "name": "Loei (LOE)"},
            {"id": "LGU", "name": "Logan-Cache (LGU)"},
            {"id": "RJL", "name": "Logroño-Agoncillo Airport (RJL)"},
            {"id": "LIW", "name": "Loikaw Airport (LIW)"},
            {"id": "LKG", "name": "Lokichoggio Airport (LKG)"},
            {"id": "GAU", "name": "Lokpriya Gopinath Bordoloi International Airport (GAU)"},
            {"id": "LOP", "name": "Lombok International Airport (LOP)"},
            {"id": "LPC", "name": "Lompoc Airport (LPC)"},
            {"id": "LNV", "name": "Londolovit Airport (LNV)"},
            {"id": "LDZ", "name": "Londolozi (LDZ)"},
            {"id": "YXU", "name": "London (YXU)"},
            {"id": "QQK", "name": "London - Kings Cross (QQK)"},
            {"id": "STP", "name": "London St Pancras (STP)"},
            {"id": "LOZ", "name": "London-Corbin Airport-MaGee Field (LOZ)"},
            {"id": "LDB", "name": "Londrina (LDB)"},
            {"id": "CXO", "name": "Lone Star Executive (CXO)"},
            {"id": "LKH", "name": "Long Akah Airport (LKH)"},
            {"id": "LPU", "name": "Long Apung Airport (LPU)"},
            {"id": "LBP", "name": "Long Banga Airport (LBP)"},
            {"id": "LBW", "name": "Long Bawan Airport (LBW)"},
            {"id": "LGB", "name": "Long Beach (LGB)"},
            {"id": "ISP", "name": "Long Island Mac Arthur (ISP)"},
            {"id": "NY9", "name": "Long Lake (NY9)"},
            {"id": "LGL", "name": "Long Lellang Airport (LGL)"},
            {"id": "ODN", "name": "Long Seridan Airport (ODN)"},
            {"id": "LOD", "name": "Longana Airport (LOD)"},
            {"id": "KWE", "name": "Longdongbao (KWE)"},
            {"id": "LRE", "name": "Longreach Airport (LRE)"},
            {"id": "DIJ", "name": "Longvic (DIJ)"},
            {"id": "LCX", "name": "Longyan Airport (LCX)"},
            {"id": "LYR", "name": "Longyear (LYR)"},
            {"id": "LNE", "name": "Lonorore Airport (LNE)"},
            {"id": "LPS", "name": "Lopez Island Airport (LPS)"},
            {"id": "LPR", "name": "Lorain County Regional Airport (LPR)"},
            {"id": "LDH", "name": "Lord Howe Island Airport (LDH)"},
            {"id": "LTO", "name": "Loreto Intl (LTO)"},
            {"id": "LAM", "name": "Los Alamos Airport (LAM)"},
            {"id": "LAX", "name": "Los Angeles Intl (LAX)"},
            {"id": "SJD", "name": "Los Cabos Intl (SJD)"},
            {"id": "RVE", "name": "Los Colonizadores Airport (RVE)"},
            {"id": "MTR", "name": "Los Garzones (MTR)"},
            {"id": "LSZ", "name": "Losinj Airport (LSZ)"},
            {"id": "LMO", "name": "Lossiemouth (LMO)"},
            {"id": "LNN", "name": "Lost Nation Municipal Airport (LNN)"},
            {"id": "LSA", "name": "Losuia Airport (LSA)"},
            {"id": "DIS", "name": "Loubomo Airport (DIS)"},
            {"id": "LPY", "name": "Loudes (LPY)"},
            {"id": "MSY", "name": "Louis Armstrong New Orleans Intl (MSY)"},
            {"id": "SDF", "name": "Louisville International Airport (SDF)"},
            {"id": "LDE", "name": "Lourdes (LDE)"},
            {"id": "YBX", "name": "Lourdes De Blanc Sablon Airport (YBX)"},
            {"id": "CHA", "name": "Lovell Fld (CHA)"},
            {"id": "24C", "name": "Lowell City Airport (24C)"},
            {"id": "KWG", "name": "Lozuvatka International Airport (KWG)"},
            {"id": "LAD", "name": "Luanda 4 De Fevereiro (LAD)"},
            {"id": "LXG", "name": "Luang Namtha (LXG)"},
            {"id": "LPQ", "name": "Luang Phabang Intl (LPQ)"},
            {"id": "LBX", "name": "Lubang Community Airport (LBX)"},
            {"id": "SDD", "name": "Lubango (SDD)"},
            {"id": "LBB", "name": "Lubbock Preston Smith Intl (LBB)"},
            {"id": "LBC", "name": "Lubeck Blankensee (LBC)"},
            {"id": "LUZ", "name": "Lublin (LUZ)"},
            {"id": "FBM", "name": "Lubumbashi Intl (FBM)"},
            {"id": "LBZ", "name": "Lucapa Airport (LBZ)"},
            {"id": "ERY", "name": "Luce County Airport (ERY)"},
            {"id": "LKO", "name": "Lucknow (LKO)"},
            {"id": "LUD", "name": "Luderitz Airport (LUD)"},
            {"id": "LUH", "name": "Ludhiana (LUH)"},
            {"id": "LUO", "name": "Luena (LUO)"},
            {"id": "LUG", "name": "Lugano (LUG)"},
            {"id": "VSG", "name": "Luhansk International Airport (VSG)"},
            {"id": "SJU", "name": "Luis Munoz Marin Intl (SJU)"},
            {"id": "LUF", "name": "Luke Afb (LUF)"},
            {"id": "LUA", "name": "Lukla (LUA)"},
            {"id": "NKG", "name": "Lukou (NKG)"},
            {"id": "HFE", "name": "Luogang (HFE)"},
            {"id": "LYA", "name": "Luoyang Airport (LYA)"},
            {"id": "MLA", "name": "Luqa (MLA)"},
            {"id": "LUN", "name": "Lusaka Intl (LUN)"},
            {"id": "LTN", "name": "Luton (LTN)"},
            {"id": "YSG", "name": "Lutselk'e Airport (YSG)"},
            {"id": "UKC", "name": "Lutsk (UKC)"},
            {"id": "LUX", "name": "Luxembourg (LUX)"},
            {"id": "LXR", "name": "Luxor Intl (LXR)"},
            {"id": "LZO", "name": "Luzhou Airport (LZO)"},
            {"id": "LWO", "name": "Lviv Intl (LWO)"},
            {"id": "LYC", "name": "Lycksele (LYC)"},
            {"id": "LYX", "name": "Lydd (LYX)"},
            {"id": "LYH", "name": "Lynchburg Regional Preston Glenn Field (LYH)"},
            {"id": "38W", "name": "Lynden Airport (38W)"},
            {"id": "NAS", "name": "Lynden Pindling Intl (NAS)"},
            {"id": "LYE", "name": "Lyneham (LYE)"},
            {"id": "YYL", "name": "Lynn Lake (YYL)"},
            {"id": "XYD", "name": "Lyon Part-Dieu Railway (XYD)"},
            {"id": "GNI", "name": "Lyudao (GNI)"},
            {"id": "BTS", "name": "M R Stefanik (BTS)"},
            {"id": "VAM", "name": "Maamigili Airport (VAM)"},
            {"id": "MST", "name": "Maastricht (MST)"},
            {"id": "USI", "name": "Mabaruma Airport (USI)"},
            {"id": "UBB", "name": "Mabuiag Island Airport (UBB)"},
            {"id": "MCP", "name": "Macapa (MCP)"},
            {"id": "XZM", "name": "Macau Ferry Pier (XZM)"},
            {"id": "MFM", "name": "Macau Intl (MFM)"},
            {"id": "MEA", "name": "Macaé Airport (MEA)"},
            {"id": "MCF", "name": "Macdill Afb (MCF)"},
            {"id": "MFT", "name": "Machu Pichu Airport (MFT)"},
            {"id": "MKY", "name": "Mackay (MKY)"},
            {"id": "YZY", "name": "Mackenzie Airport (YZY)"},
            {"id": "MCD", "name": "Mackinac Island Airport (MCD)"},
            {"id": "MQB", "name": "Macomb Municipal Airport (MQB)"},
            {"id": "CEB", "name": "Mactan Cebu Intl (CEB)"},
            {"id": "MAG", "name": "Madang (MAG)"},
            {"id": "FNC", "name": "Madeira (FNC)"},
            {"id": "MAE", "name": "Madera Municipal Airport (MAE)"},
            {"id": "52A", "name": "Madison GA Municipal Airport (52A)"},
            {"id": "IXM", "name": "Madurai (IXM)"},
            {"id": "HGN", "name": "Mae Hong Son (HGN)"},
            {"id": "MAQ", "name": "Mae Sot Airport (MAQ)"},
            {"id": "MFA", "name": "Mafia (MFA)"},
            {"id": "%u0", "name": "Magas (%u0)"},
            {"id": "CSO", "name": "Magdeburg-Cochstedt (CSO)"},
            {"id": "GEA", "name": "Magenta (GEA)"},
            {"id": "TWF", "name": "Magic Valley Regional Airport (TWF)"},
            {"id": "MQF", "name": "Magnitogorsk (MQF)"},
            {"id": "MZG", "name": "Magong (MZG)"},
            {"id": "MWQ", "name": "Magwe (MWQ)"},
            {"id": "RPN", "name": "Mahanaim I Ben Yaakov (RPN)"},
            {"id": "MHA", "name": "Mahdia Airport (MHA)"},
            {"id": "EUG", "name": "Mahlon Sweet Fld (EUG)"},
            {"id": "MRX", "name": "Mahshahr (MRX)"},
            {"id": "MNK", "name": "Maiana Airport (MNK)"},
            {"id": "MIU", "name": "Maiduguri (MIU)"},
            {"id": "MMZ", "name": "Maimana (MMZ)"},
            {"id": "SBG", "name": "Maimun Saleh (SBG)"},
            {"id": "MXT", "name": "Maintirano Airport (MXT)"},
            {"id": "QMZ", "name": "Mainz (QMZ)"},
            {"id": "QFZ", "name": "Mainz Finthen (QFZ)"},
            {"id": "MMO", "name": "Maio (MMO)"},
            {"id": "MTL", "name": "Maitland Airport (MTL)"},
            {"id": "MJE", "name": "Majkin Airport (MJE)"},
            {"id": "ICK", "name": "Majoor Henry Fernandes Airport (ICK)"},
            {"id": "VAG", "name": "Major Brigadeiro Trompowsky (VAG)"},
            {"id": "GVT", "name": "Majors (GVT)"},
            {"id": "MQX", "name": "Makale (MQX)"},
            {"id": "SKG", "name": "Makedonia (SKG)"},
            {"id": "MKP", "name": "Makemo (MKP)"},
            {"id": "MTK", "name": "Makin Airport (MTK)"},
            {"id": "YMN", "name": "Makkovik Airport (YMN)"},
            {"id": "MKU", "name": "Makokou (MKU)"},
            {"id": "MDI", "name": "Makurdi (MDI)"},
            {"id": "SSG", "name": "Malabo (SSG)"},
            {"id": "MKZ", "name": "Malacca (MKZ)"},
            {"id": "MLD", "name": "Malad City (MLD)"},
            {"id": "AGP", "name": "Malaga (AGP)"},
            {"id": "MAK", "name": "Malakal (MAK)"},
            {"id": "AAM", "name": "Malamala Airport (AAM)"},
            {"id": "MEG", "name": "Malanje (MEG)"},
            {"id": "LGS", "name": "Malargue (LGS)"},
            {"id": "MLE", "name": "Male Intl (MLE)"},
            {"id": "ARD", "name": "Mali Airport (ARD)"},
            {"id": "LSW", "name": "Malikus Saleh Airport (LSW)"},
            {"id": "MYD", "name": "Malindi Airport (MYD)"},
            {"id": "XMC", "name": "Mallacoota Airport (XMC)"},
            {"id": "KAN", "name": "Mallam Aminu Intl (KAN)"},
            {"id": "MAV", "name": "Maloelap Island Airport (MAV)"},
            {"id": "PTF", "name": "Malolo Lailai Island Airport (PTF)"},
            {"id": "MXP", "name": "Malpensa (MXP)"},
            {"id": "MPU", "name": "Mamitupo (MPU)"},
            {"id": "MMH", "name": "Mammoth Yosemite Airport (MMH)"},
            {"id": "JMY", "name": "Mammy Yoko Heliport (JMY)"},
            {"id": "WMP", "name": "Mampikony Airport (WMP)"},
            {"id": "MJC", "name": "Man (MJC)"},
            {"id": "MNF", "name": "Mana Island Airport (MNF)"},
            {"id": "MGA", "name": "Managua Intl (MGA)"},
            {"id": "WVK", "name": "Manakara (WVK)"},
            {"id": "NGX", "name": "Manang (NGX)"},
            {"id": "MNJ", "name": "Mananjary (MNJ)"},
            {"id": "TEU", "name": "Manapouri (TEU)"},
            {"id": "FRU", "name": "Manas (FRU)"},
            {"id": "MNZ", "name": "Manassas (MNZ)"},
            {"id": "MAN", "name": "Manchester (MAN)"},
            {"id": "MHT", "name": "Manchester Regional Airport (MHT)"},
            {"id": "MDL", "name": "Mandalay Intl (MDL)"},
            {"id": "CEQ", "name": "Mandelieu (CEQ)"},
            {"id": "COG", "name": "Mandinga Airport (COG)"},
            {"id": "WMA", "name": "Mandritsara Airport (WMA)"},
            {"id": "MGS", "name": "Mangaia Island Airport (MGS)"},
            {"id": "IXE", "name": "Mangalore (IXE)"},
            {"id": "LUM", "name": "Mangshi Airport (LUM)"},
            {"id": "MHK", "name": "Manhattan Reigonal (MHK)"},
            {"id": "MNX", "name": "Manicore (MNX)"},
            {"id": "XMH", "name": "Manihi (XMH)"},
            {"id": "MHX", "name": "Manihiki Island Airport (MHX)"},
            {"id": "JSU", "name": "Maniitsoq Airport (JSU)"},
            {"id": "MNG", "name": "Maningrida Airport (MNG)"},
            {"id": "MBL", "name": "Manistee County-Blacker Airport (MBL)"},
            {"id": "YEM", "name": "Manitoulin East (YEM)"},
            {"id": "YMG", "name": "Manitouwadge (YMG)"},
            {"id": "YMW", "name": "Maniwaki (YMW)"},
            {"id": "MJA", "name": "Manja Airport (MJA)"},
            {"id": "MLY", "name": "Manley Hot Springs Airport (MLY)"},
            {"id": "MHG", "name": "Mannheim City (MHG)"},
            {"id": "ZMA", "name": "Mannheim Railway (ZMA)"},
            {"id": "KMO", "name": "Manokotak Airport (KMO)"},
            {"id": "MFD", "name": "Mansfield Lahm Regional (MFD)"},
            {"id": "1B9", "name": "Mansfield Municipal (1B9)"},
            {"id": "MSE", "name": "Manston (MSE)"},
            {"id": "AJY", "name": "Manu Dayak (AJY)"},
            {"id": "NZH", "name": "Manzhouli (NZH)"},
            {"id": "MXS", "name": "Maota Airport (MXS)"},
            {"id": "MPM", "name": "Maputo (MPM)"},
            {"id": "ZCO", "name": "Maquehue (ZCO)"},
            {"id": "MDQ", "name": "Mar Del Plata (MDQ)"},
            {"id": "MRE", "name": "Mara Serena Airport (MRE)"},
            {"id": "MAB", "name": "Maraba (MAB)"},
            {"id": "MFQ", "name": "Maradi (MFQ)"},
            {"id": "MZK", "name": "Marakei Airport (MZK)"},
            {"id": "AVW", "name": "Marana Regional (AVW)"},
            {"id": "YSP", "name": "Marathon (YSP)"},
            {"id": "RUS", "name": "Marau Airport (RUS)"},
            {"id": "RIV", "name": "March Arb (RIV)"},
            {"id": "RDZ", "name": "Marcillac (RDZ)"},
            {"id": "MRK", "name": "Marco Islands (MRK)"},
            {"id": "PAC", "name": "Marcos A Gelabert Intl (PAC)"},
            {"id": "MQM", "name": "Mardin Airport (MQM)"},
            {"id": "MEE", "name": "Mare (MEE)"},
            {"id": "SLZ", "name": "Marechal Cunha Machado Intl (SLZ)"},
            {"id": "CGB", "name": "Marechal Rondon (CGB)"},
            {"id": "MGH", "name": "Margate (MGH)"},
            {"id": "KNF", "name": "Marham (KNF)"},
            {"id": "LSQ", "name": "Maria Dolores (LSQ)"},
            {"id": "BRX", "name": "Maria Montez Intl (BRX)"},
            {"id": "NZA", "name": "Maria Reiche Neuman Airport (NZA)"},
            {"id": "GAO", "name": "Mariana Grajales (GAO)"},
            {"id": "MBX", "name": "Maribor (MBX)"},
            {"id": "MHQ", "name": "Mariehamn (MHQ)"},
            {"id": "EBA", "name": "Marina Di Campo (EBA)"},
            {"id": "OAR", "name": "Marina Muni (OAR)"},
            {"id": "MRQ", "name": "Marinduque Airport (MRQ)"},
            {"id": "MOC", "name": "Mario Ribeiro (MOC)"},
            {"id": "MPY", "name": "Maripasoula Airport (MPY)"},
            {"id": "MPI", "name": "MariposaYosemite (MPI)"},
            {"id": "CUE", "name": "Mariscal Lamar (CUE)"},
            {"id": "UIO", "name": "Mariscal Sucre Intl (UIO)"},
            {"id": "MPW", "name": "Mariupol International Airport (MPW)"},
            {"id": "2A0", "name": "Mark Anton Airport (2A0)"},
            {"id": "ADJ", "name": "Marka Intl (ADJ)"},
            {"id": "NU8", "name": "Markham (NU8)"},
            {"id": "KVM", "name": "Markovo Airport (KVM)"},
            {"id": "OAL", "name": "Marktoberdorf BF (OAL)"},
            {"id": "MOS", "name": "Marktoberdorf Schule (MOS)"},
            {"id": "OMM", "name": "Marmul (OMM)"},
            {"id": "WMN", "name": "Maroantsetra (WMN)"},
            {"id": "RMF", "name": "Marsa Alam Intl (RMF)"},
            {"id": "MHH", "name": "Marsh Harbour (MHH)"},
            {"id": "FRI", "name": "Marshall Aaf (FRI)"},
            {"id": "MLL", "name": "Marshall Don Hunter Sr. Airport (MLL)"},
            {"id": "MAJ", "name": "Marshall Islands Intl (MAJ)"},
            {"id": "GHG", "name": "Marshfield Municipal Airport (GHG)"},
            {"id": "MFI", "name": "Marshfield Municipal Airport (MFI)"},
            {"id": "MVY", "name": "Martha's Vineyard (MVY)"},
            {"id": "1A3", "name": "Martin Campbell Field Airport (1A3)"},
            {"id": "MTN", "name": "Martin State (MTN)"},
            {"id": "MUR", "name": "Marudi (MUR)"},
            {"id": "MYP", "name": "Mary Airport (MYP)"},
            {"id": "YMH", "name": "Mary's Harbour Airport (YMH)"},
            {"id": "MBH", "name": "Maryborough Airport (MBH)"},
            {"id": "MII", "name": "Marília Airport (MII)"},
            {"id": "MBT", "name": "Masbate Airport (MBT)"},
            {"id": "MHD", "name": "Mashhad (MHD)"},
            {"id": "MSH", "name": "Masirah (MSH)"},
            {"id": "MCW", "name": "Mason City Municipal (MCW)"},
            {"id": "MSW", "name": "Massawa Intl (MSW)"},
            {"id": "MSS", "name": "Massena Intl Richards Fld (MSS)"},
            {"id": "ZMT", "name": "Masset Airport (ZMT)"},
            {"id": "MRO", "name": "Masterton (MRO)"},
            {"id": "MVZ", "name": "Masvingo Intl (MVZ)"},
            {"id": "NFO", "name": "Mata'aho Airport (NFO)"},
            {"id": "YNM", "name": "Matagami (YNM)"},
            {"id": "MVT", "name": "Mataiva (MVT)"},
            {"id": "MWK", "name": "Matak Airport (MWK)"},
            {"id": "IRP", "name": "Matari (IRP)"},
            {"id": "IPC", "name": "Mataveri Intl (IPC)"},
            {"id": "PEI", "name": "Matecana (PEI)"},
            {"id": "TVU", "name": "Matei Airport (TVU)"},
            {"id": "MTS", "name": "Matsapha (MTS)"},
            {"id": "MFK", "name": "Matsu Beigan Airport (MFK)"},
            {"id": "LZN", "name": "Matsu Nangan Airport (LZN)"},
            {"id": "MMJ", "name": "Matsumoto (MMJ)"},
            {"id": "MYJ", "name": "Matsuyama (MYJ)"},
            {"id": "HRI", "name": "Mattala Rajapaksa Intl. (HRI)"},
            {"id": "IGA", "name": "Matthew Town (IGA)"},
            {"id": "MUN", "name": "Maturin (MUN)"},
            {"id": "WGP", "name": "Mau Hau (WGP)"},
            {"id": "MBZ", "name": "Maues Airport (MBZ)"},
            {"id": "MUK", "name": "Mauke Airport (MUK)"},
            {"id": "MUB", "name": "Maun (MUB)"},
            {"id": "CER", "name": "Maupertus (CER)"},
            {"id": "MAU", "name": "Maupiti (MAU)"},
            {"id": "MNU", "name": "Mawlamyine Airport (MNU)"},
            {"id": "AVI", "name": "Maximo Gomez (AVI)"},
            {"id": "MXF", "name": "Maxwell Afb (MXF)"},
            {"id": "BZV", "name": "Maya Maya (BZV)"},
            {"id": "MYG", "name": "Mayaguana (MYG)"},
            {"id": "YMA", "name": "Mayo (YMA)"},
            {"id": "STD", "name": "Mayor Buenaventura Vivas (STD)"},
            {"id": "CJA", "name": "Mayor General FAP Armando Revoredo Iglesias Airport (CJA)"},
            {"id": "MYB", "name": "Mayumba Airport (MYB)"},
            {"id": "DCM", "name": "Mazamet (DCM)"},
            {"id": "MZR", "name": "Mazar I Sharif (MZR)"},
            {"id": "MDK", "name": "Mbandaka (MDK)"},
            {"id": "SSY", "name": "Mbanza Congo (SSY)"},
            {"id": "MBS", "name": "Mbs Intl (MBS)"},
            {"id": "MJM", "name": "Mbuji Mayi (MJM)"},
            {"id": "MLC", "name": "Mc Alester Rgnl (MLC)"},
            {"id": "MFE", "name": "Mc Allen Miller Intl (MFE)"},
            {"id": "LAS", "name": "Mc Carran Intl (LAS)"},
            {"id": "TCM", "name": "Mc Chord Afb (TCM)"},
            {"id": "MCC", "name": "Mc Clellan Afld (MCC)"},
            {"id": "IAB", "name": "Mc Connell Afb (IAB)"},
            {"id": "TYS", "name": "Mc Ghee Tyson (TYS)"},
            {"id": "WRI", "name": "Mc Guire Afb (WRI)"},
            {"id": "MKL", "name": "Mc Kellar Sipes Rgnl (MKL)"},
            {"id": "MMV", "name": "Mc Minnville Muni (MMV)"},
            {"id": "MCV", "name": "McArthur River Mine Airport (MCV)"},
            {"id": "MYL", "name": "McCall Municipal Airport (MYL)"},
            {"id": "MXY", "name": "McCarthy Airport (MXY)"},
            {"id": "CLD", "name": "McClellan-Palomar Airport (CLD)"},
            {"id": "MCK", "name": "McCook Regional Airport (MCK)"},
            {"id": "HQU", "name": "McDuffie County Airport (HQU)"},
            {"id": "MCG", "name": "McGrath Airport (MCG)"},
            {"id": "MCL", "name": "McKinley National Park Airport (MCL)"},
            {"id": "SSI", "name": "McKinnon Airport (SSI)"},
            {"id": "MMI", "name": "McMinn Co (MMI)"},
            {"id": "SLE", "name": "McNary Field (SLE)"},
            {"id": "YLJ", "name": "Meadow Lake (YLJ)"},
            {"id": "BFL", "name": "Meadows Fld (BFL)"},
            {"id": "YXH", "name": "Medicine Hat (YXH)"},
            {"id": "RYN", "name": "Medis (RYN)"},
            {"id": "MPL", "name": "Mediterranee (MPL)"},
            {"id": "MKR", "name": "Meekatharra Airport (MKR)"},
            {"id": "KVA", "name": "Megas Alexandros Intl (KVA)"},
            {"id": "MVV", "name": "Megeve Airport (MVV)"},
            {"id": "MEY", "name": "Meghauli Airport (MEY)"},
            {"id": "MEH", "name": "Mehamn (MEH)"},
            {"id": "THR", "name": "Mehrabad Intl (THR)"},
            {"id": "CGX", "name": "Meigs Field (CGX)"},
            {"id": "HAK", "name": "Meilan (HAK)"},
            {"id": "MXZ", "name": "Meixian Airport (MXZ)"},
            {"id": "MJB", "name": "Mejit Atoll Airport (MJB)"},
            {"id": "MKS", "name": "Mekane Salam Airport (MKS)"},
            {"id": "MYU", "name": "Mekoryuk Airport (MYU)"},
            {"id": "MEB", "name": "Melbourne Essendon (MEB)"},
            {"id": "MEL", "name": "Melbourne Intl (MEL)"},
            {"id": "MLB", "name": "Melbourne Intl (MLB)"},
            {"id": "MBW", "name": "Melbourne Moorabbin (MBW)"},
            {"id": "MLN", "name": "Melilla (MLN)"},
            {"id": "DOM", "name": "Melville Hall (DOM)"},
            {"id": "MMB", "name": "Memanbetsu (MMB)"},
            {"id": "HOT", "name": "Memorial Field (HOT)"},
            {"id": "MEM", "name": "Memphis Intl (MEM)"},
            {"id": "RAK", "name": "Menara (RAK)"},
            {"id": "DEE", "name": "Mendeleevo (DEE)"},
            {"id": "MDU", "name": "Mendi Airport (MDU)"},
            {"id": "MNM", "name": "Menominee Marinette Twin Co (MNM)"},
            {"id": "SPP", "name": "Menongue (SPP)"},
            {"id": "MAH", "name": "Menorca (MAH)"},
            {"id": "MCE", "name": "Merced Municipal Airport (MCE)"},
            {"id": "PSE", "name": "Mercedita (PSE)"},
            {"id": "BLF", "name": "Mercer County Airport (BLF)"},
            {"id": "MFX", "name": "Meribel Airport (MFX)"},
            {"id": "NMM", "name": "Meridian Nas (NMM)"},
            {"id": "BOD", "name": "Merignac (BOD)"},
            {"id": "MIM", "name": "Merimbula Airport (MIM)"},
            {"id": "CDV", "name": "Merle K Mudhole Smith (CDV)"},
            {"id": "MRI", "name": "Merrill Fld (MRI)"},
            {"id": "COI", "name": "Merritt Island Airport (COI)"},
            {"id": "MUH", "name": "Mersa Matruh (MUH)"},
            {"id": "MZH", "name": "Merzifon (MZH)"},
            {"id": "FFZ", "name": "Mesa Falcon Field (FFZ)"},
            {"id": "MEZ", "name": "Messina (MEZ)"},
            {"id": "MTM", "name": "Metlakatla Seaplane Base (MTM)"},
            {"id": "OAK", "name": "Metropolitan Oakland Intl (OAK)"},
            {"id": "ETZ", "name": "Metz Nancy Lorraine (ETZ)"},
            {"id": "VNE", "name": "Meucon (VNE)"},
            {"id": "LXY", "name": "Mexia - Limestone County Airport (LXY)"},
            {"id": "NCY", "name": "Meythet (NCY)"},
            {"id": "MFU", "name": "Mfuwe (MFU)"},
            {"id": "MIA", "name": "Miami Intl (MIA)"},
            {"id": "MPB", "name": "Miami Seaplane Base (MPB)"},
            {"id": "OXD", "name": "Miami University Airport (OXD)"},
            {"id": "ZVA", "name": "Miandrivazo (ZVA)"},
            {"id": "MIG", "name": "Mianyang Airport (MIG)"},
            {"id": "MGC", "name": "Michigan City Municipal Airport (MGC)"},
            {"id": "GLH", "name": "Mid Delta Regional Airport (GLH)"},
            {"id": "PKB", "name": "Mid-Ohio Valley Regional Airport (PKB)"},
            {"id": "MDS", "name": "Middle Caicos Airport (MDS)"},
            {"id": "MCN", "name": "Middle Georgia Rgnl (MCN)"},
            {"id": "MAF", "name": "Midland Intl (MAF)"},
            {"id": "MDY", "name": "Midway Atoll (MDY)"},
            {"id": "KLD", "name": "Migalovo (KLD)"},
            {"id": "CND", "name": "Mihail Kogalniceanu (CND)"},
            {"id": "MIK", "name": "Mikkeli (MIK)"},
            {"id": "JMK", "name": "Mikonos (JMK)"},
            {"id": "MHZ", "name": "Mildenhall (MHZ)"},
            {"id": "MQL", "name": "Mildura Airport (MQL)"},
            {"id": "MFN", "name": "Milford Sound Airport (MFN)"},
            {"id": "MIJ", "name": "Mili Island Airport (MIJ)"},
            {"id": "MGT", "name": "Milingimbi Airport (MGT)"},
            {"id": "NQA", "name": "Millington Rgnl Jetport (NQA)"},
            {"id": "MLT", "name": "Millinocket Muni (MLT)"},
            {"id": "MIV", "name": "Millville Muni (MIV)"},
            {"id": "MLO", "name": "Milos (MLO)"},
            {"id": "MQH", "name": "Minacu Airport (MQH)"},
            {"id": "MMD", "name": "Minami Daito (MMD)"},
            {"id": "PDG", "name": "Minangkabau (PDG)"},
            {"id": "MTT", "name": "Minatitlan (MTT)"},
            {"id": "MHM", "name": "Minchumina Airport (MHM)"},
            {"id": "MWL", "name": "Mineral Wells (MWL)"},
            {"id": "MRV", "name": "Mineralnyye Vody (MRV)"},
            {"id": "NHD", "name": "Minhad HB (NHD)"},
            {"id": "EZE", "name": "Ministro Pistarini (EZE)"},
            {"id": "NVT", "name": "Ministro Victor Konder Intl (NVT)"},
            {"id": "MXJ", "name": "Minna New (MXJ)"},
            {"id": "MSP", "name": "Minneapolis St Paul Intl (MSP)"},
            {"id": "MIB", "name": "Minot Afb (MIB)"},
            {"id": "MOT", "name": "Minot Intl (MOT)"},
            {"id": "MHP", "name": "Minsk 1 (MHP)"},
            {"id": "MSQ", "name": "Minsk 2 (MSQ)"},
            {"id": "MNT", "name": "Minto Airport (MNT)"},
            {"id": "MQC", "name": "Miquelon (MQC)"},
            {"id": "NKX", "name": "Miramar Mcas (NKX)"},
            {"id": "YCH", "name": "Miramichi (YCH)"},
            {"id": "EPL", "name": "Mirecourt (EPL)"},
            {"id": "MYY", "name": "Miri (MYY)"},
            {"id": "MJZ", "name": "Mirny (MJZ)"},
            {"id": "MSJ", "name": "Misawa Ab (MSJ)"},
            {"id": "MIS", "name": "Misima Island Airport (MIS)"},
            {"id": "MRA", "name": "Misratah Airport (MRA)"},
            {"id": "LVM", "name": "Mission Field Airport (LVM)"},
            {"id": "MSO", "name": "Missoula Intl (MSO)"},
            {"id": "MOI", "name": "Mitiaro Island Airport (MOI)"},
            {"id": "MJI", "name": "Mitiga Airport (MJI)"},
            {"id": "MJT", "name": "Mitilini (MJT)"},
            {"id": "MYE", "name": "Miyakejima Airport (MYE)"},
            {"id": "MMY", "name": "Miyako (MMY)"},
            {"id": "KMI", "name": "Miyazaki (KMI)"},
            {"id": "MTF", "name": "Mizan Teferi Airport (MTF)"},
            {"id": "MBD", "name": "Mmabatho International Airport (MBD)"},
            {"id": "MFJ", "name": "Moala Airport (MFJ)"},
            {"id": "BFM", "name": "Mobile Downtown (BFM)"},
            {"id": "MOB", "name": "Mobile Rgnl (MOB)"},
            {"id": "MZB", "name": "Mocimboa Da Praia (MZB)"},
            {"id": "MOD", "name": "Modesto City Co Harry Sham (MOD)"},
            {"id": "MJD", "name": "Moenjodaro (MJD)"},
            {"id": "NUQ", "name": "Moffett Federal Afld (NUQ)"},
            {"id": "ESU", "name": "Mogador Airport (ESU)"},
            {"id": "MVQ", "name": "Mogilev Airport (MVQ)"},
            {"id": "CZL", "name": "Mohamed Boudiaf Intl (CZL)"},
            {"id": "CMN", "name": "Mohammed V Intl (CMN)"},
            {"id": "YMS", "name": "Moises Benzaquen Rengifo (YMS)"},
            {"id": "MHV", "name": "Mojave (MHV)"},
            {"id": "MPK", "name": "Mokpo Airport (MPK)"},
            {"id": "OKU", "name": "Mokuti Lodge Airport (OKU)"},
            {"id": "MKK", "name": "Molokai (MKK)"},
            {"id": "MBA", "name": "Mombasa Moi Intl (MBA)"},
            {"id": "MAS", "name": "Momote Airport (MAS)"},
            {"id": "MCM", "name": "Monaco (MCM)"},
            {"id": "MBE", "name": "Monbetsu (MBE)"},
            {"id": "MGL", "name": "Monchengladbach (MGL)"},
            {"id": "LOV", "name": "Monclova Intl (LOV)"},
            {"id": "MOG", "name": "Mong Hsat (MOG)"},
            {"id": "MNY", "name": "Mono Airport (MNY)"},
            {"id": "BMG", "name": "Monroe County Airport (BMG)"},
            {"id": "EQY", "name": "Monroe Reqional Airport (EQY)"},
            {"id": "MLU", "name": "Monroe Rgnl (MLU)"},
            {"id": "ROB", "name": "Monrovia Roberts Intl (ROB)"},
            {"id": "MLW", "name": "Monrovia Spriggs Payne (MLW)"},
            {"id": "YYY", "name": "Mont Joli (YYY)"},
            {"id": "XMU", "name": "Montbeugny (XMU)"},
            {"id": "MRY", "name": "Monterey Peninsula (MRY)"},
            {"id": "GAI", "name": "Montgomery County Airpark (GAI)"},
            {"id": "MYF", "name": "Montgomery Field (MYF)"},
            {"id": "MGM", "name": "Montgomery Regional Airport  (MGM)"},
            {"id": "VBS", "name": "Montichiari (VBS)"},
            {"id": "SNR", "name": "Montoir (SNR)"},
            {"id": "YMX", "name": "Montreal Intl Mirabel (YMX)"},
            {"id": "MTJ", "name": "Montrose Regional Airport (MTJ)"},
            {"id": "VAD", "name": "Moody Afb (VAD)"},
            {"id": "MOO", "name": "Moomba (MOO)"},
            {"id": "SOP", "name": "Moore County Airport (SOP)"},
            {"id": "MOZ", "name": "Moorea (MOZ)"},
            {"id": "YMJ", "name": "Moose Jaw Air Vice Marshal C M Mcewen (YMJ)"},
            {"id": "YMO", "name": "Moosonee (YMO)"},
            {"id": "MKQ", "name": "Mopah (MKQ)"},
            {"id": "MXX", "name": "Mora (MXX)"},
            {"id": "TVA", "name": "Morafenobe Airport (TVA)"},
            {"id": "MOV", "name": "Moranbah Airport (MOV)"},
            {"id": "MWB", "name": "Morawa Airport (MWB)"},
            {"id": "MRZ", "name": "Moree Airport (MRZ)"},
            {"id": "O03", "name": "Morgantown Airport (O03)"},
            {"id": "MGW", "name": "Morgantown Muni Walter L Bill Hart Fld (MGW)"},
            {"id": "ONG", "name": "Mornington Island Airport (ONG)"},
            {"id": "MXH", "name": "Moro Airport (MXH)"},
            {"id": "MXM", "name": "Morombe (MXM)"},
            {"id": "OZP", "name": "Moron Ab (OZP)"},
            {"id": "MOQ", "name": "Morondava (MOQ)"},
            {"id": "MMU", "name": "Morristown Municipal Airport (MMU)"},
            {"id": "MVL", "name": "Morrisville Stowe State Airport (MVL)"},
            {"id": "MYA", "name": "Moruya Airport (MYA)"},
            {"id": "KMY", "name": "Moser Bay Seaplane Base (KMY)"},
            {"id": "TIM", "name": "Moses Kilangin (TIM)"},
            {"id": "MSU", "name": "Moshoeshoe I Intl (MSU)"},
            {"id": "OSR", "name": "Mosnov (OSR)"},
            {"id": "RYG", "name": "Moss (RYG)"},
            {"id": "OMO", "name": "Mostar (OMO)"},
            {"id": "OSB", "name": "Mosul International Airport (OSB)"},
            {"id": "MTV", "name": "Mota Lava Airport (MTV)"},
            {"id": "06A", "name": "Moton Field Municipal Airport (06A)"},
            {"id": "MZP", "name": "Motueka (MZP)"},
            {"id": "MJL", "name": "Mouilla Ville Airport (MJL)"},
            {"id": "ERH", "name": "Moulay Ali Cherif (ERH)"},
            {"id": "MGR", "name": "Moultrie Municipal Airport (MGR)"},
            {"id": "MQQ", "name": "Moundou (MQQ)"},
            {"id": "GTN", "name": "Mount Cook (GTN)"},
            {"id": "MGB", "name": "Mount Gambier Airport (MGB)"},
            {"id": "HGU", "name": "Mount Hagen (HGU)"},
            {"id": "MHU", "name": "Mount Hotham Airport (MHU)"},
            {"id": "ISA", "name": "Mount Isa (ISA)"},
            {"id": "WME", "name": "Mount Keith (WME)"},
            {"id": "MMG", "name": "Mount Magnet Airport (MMG)"},
            {"id": "MPN", "name": "Mount Pleasant (MPN)"},
            {"id": "LRO", "name": "Mount Pleasant Regional-Faison Field (LRO)"},
            {"id": "MWP", "name": "Mountain Airport (MWP)"},
            {"id": "MUO", "name": "Mountain Home Afb (MUO)"},
            {"id": "U76", "name": "Mountain Home Municipal Airport (U76)"},
            {"id": "MOU", "name": "Mountain Village Airport (MOU)"},
            {"id": "MOW", "name": "MOW (MOW)"},
            {"id": "OYL", "name": "Moyale Airport (OYL)"},
            {"id": "OYG", "name": "Moyo Airport (OYG)"},
            {"id": "FSZ", "name": "Mt. Fuji Shizuoka Airport (FSZ)"},
            {"id": "UTT", "name": "Mthatha (UTT)"},
            {"id": "MYW", "name": "Mtwara (MYW)"},
            {"id": "MWX", "name": "Muan (MWX)"},
            {"id": "MNB", "name": "Muanda (MNB)"},
            {"id": "MVS", "name": "Mucuri Airport (MVS)"},
            {"id": "MDG", "name": "Mudanjiang (MDG)"},
            {"id": "DGE", "name": "Mudgee Airport (DGE)"},
            {"id": "BMU", "name": "Muhammad Salahuddin (BMU)"},
            {"id": "MUI", "name": "Muir Aaf (MUI)"},
            {"id": "MKM", "name": "Mukah Airport (MKM)"},
            {"id": "UUD", "name": "Mukhino (UUD)"},
            {"id": "MPP", "name": "Mulatupo Airport (MPP)"},
            {"id": "MUX", "name": "Multan Intl (MUX)"},
            {"id": "MZV", "name": "Mulu (MZV)"},
            {"id": "MUA", "name": "Munda Airport (MUA)"},
            {"id": "FRS", "name": "Mundo Maya International (FRS)"},
            {"id": "MUQ", "name": "Munich HBF (MUQ)"},
            {"id": "ZMU", "name": "Munich Railway (ZMU)"},
            {"id": "CZH", "name": "Municipal (CZH)"},
            {"id": "AIK", "name": "Municipal Airport (AIK)"},
            {"id": "BUU", "name": "Municipal Airport (BUU)"},
            {"id": "LBT", "name": "Municipal Airport (LBT)"},
            {"id": "Y51", "name": "Municipal Airport (Y51)"},
            {"id": "ZPH", "name": "Municipal Airport (ZPH)"},
            {"id": "FMO", "name": "Munster Osnabruck (FMO)"},
            {"id": "MJV", "name": "Murcia San Javier (MJV)"},
            {"id": "MXV", "name": "Muren Airport (MXV)"},
            {"id": "MMK", "name": "Murmansk (MMK)"},
            {"id": "MYI", "name": "Murray Island Airport (MYI)"},
            {"id": "LOS", "name": "Murtala Muhammed (LOS)"},
            {"id": "MSR", "name": "Mus Airport (MSR)"},
            {"id": "MKG", "name": "Muskegon County Airport (MKG)"},
            {"id": "YQA", "name": "Muskoka (YQA)"},
            {"id": "MSA", "name": "Muskrat Dam Airport (MSA)"},
            {"id": "MUZ", "name": "Musoma Airport (MUZ)"},
            {"id": "MQS", "name": "Mustique (MQS)"},
            {"id": "PLW", "name": "Mutiara (PLW)"},
            {"id": "LBJ", "name": "Mutiara Ii (LBJ)"},
            {"id": "MFG", "name": "Muzaffarabad (MFG)"},
            {"id": "MVB", "name": "Mvengue (MVB)"},
            {"id": "DAR", "name": "Mwalimu Julius K Nyerere Intl (DAR)"},
            {"id": "MWZ", "name": "Mwanza (MWZ)"},
            {"id": "MGZ", "name": "Myeik (MGZ)"},
            {"id": "MYT", "name": "Myitkyina (MYT)"},
            {"id": "NLV", "name": "Mykolaiv International Airport (NLV)"},
            {"id": "MYR", "name": "Myrtle Beach Intl (MYR)"},
            {"id": "MYQ", "name": "Mysore Airport (MYQ)"},
            {"id": "ZZU", "name": "Mzuzu (ZZU)"},
            {"id": "SQH", "name": "Na-San Airport (SQH)"},
            {"id": "NBX", "name": "Nabire (NBX)"},
            {"id": "MNC", "name": "Nacala (MNC)"},
            {"id": "NAN", "name": "Nadi Intl (NAN)"},
            {"id": "NYM", "name": "Nadym Airport (NYM)"},
            {"id": "LAE", "name": "Nadzab (LAE)"},
            {"id": "WNP", "name": "Naga Airport (WNP)"},
            {"id": "NGS", "name": "Nagasaki (NGS)"},
            {"id": "NKM", "name": "Nagoya Airport (NKM)"},
            {"id": "NAH", "name": "Naha (NAH)"},
            {"id": "OKA", "name": "Naha (OKA)"},
            {"id": "YDP", "name": "Nain Airport (YDP)"},
            {"id": "WIL", "name": "Nairobi Wilson (WIL)"},
            {"id": "SHB", "name": "Nakashibetsu (SHB)"},
            {"id": "NAJ", "name": "Nakhchivan Airport (NAJ)"},
            {"id": "KOP", "name": "Nakhon Phanom (KOP)"},
            {"id": "NAK", "name": "Nakhon Ratchasima (NAK)"},
            {"id": "NST", "name": "Nakhon Si Thammarat (NST)"},
            {"id": "YQN", "name": "Nakina Airport (YQN)"},
            {"id": "NLT", "name": "Nalati (NLT)"},
            {"id": "NAL", "name": "Nalchik Airport (NAL)"},
            {"id": "NMA", "name": "Namangan Airport (NMA)"},
            {"id": "MSZ", "name": "Namibe Airport (MSZ)"},
            {"id": "NDK", "name": "Namorik Atoll Airport (NDK)"},
            {"id": "APL", "name": "Nampula (APL)"},
            {"id": "OSY", "name": "Namsos Høknesøra Airport (OSY)"},
            {"id": "NNT", "name": "Nan (NNT)"},
            {"id": "YCD", "name": "Nanaimo (YCD)"},
            {"id": "ZNA", "name": "Nanaimo Harbour Water Airport (ZNA)"},
            {"id": "NAO", "name": "Nanchong Airport (NAO)"},
            {"id": "NDC", "name": "Nanded Airport (NDC)"},
            {"id": "YSR", "name": "Nanisivik (YSR)"},
            {"id": "SHM", "name": "Nanki Shirahama (SHM)"},
            {"id": "JNN", "name": "Nanortalik Heliport (JNN)"},
            {"id": "WUS", "name": "Nanping Wuyishan Airport (WUS)"},
            {"id": "NTE", "name": "Nantes Atlantique (NTE)"},
            {"id": "NTG", "name": "Nantong Airport (NTG)"},
            {"id": "ACK", "name": "Nantucket Mem (ACK)"},
            {"id": "NNY", "name": "Nanyang Airport (NNY)"},
            {"id": "NYK", "name": "Nanyuki Civil Airport (NYK)"},
            {"id": "MWF", "name": "Naone Airport (MWF)"},
            {"id": "APC", "name": "Napa County Airport (APC)"},
            {"id": "NPE", "name": "Napier (NPE)"},
            {"id": "APF", "name": "Naples Muni (APF)"},
            {"id": "NAW", "name": "Narathiwat (NAW)"},
            {"id": "NRT", "name": "Narita Intl (NRT)"},
            {"id": "NAA", "name": "Narrabri Airport (NAA)"},
            {"id": "NRA", "name": "Narrandera Airport (NRA)"},
            {"id": "JNS", "name": "Narsaq Heliport (JNS)"},
            {"id": "UAK", "name": "Narsarsuaq (UAK)"},
            {"id": "NNM", "name": "Naryan-Mar (NNM)"},
            {"id": "NGZ", "name": "NAS Alameda (NGZ)"},
            {"id": "BNA", "name": "Nashville Intl (BNA)"},
            {"id": "ISK", "name": "Nasik Road (ISK)"},
            {"id": "PID", "name": "Nassau Paradise Island Airport (PID)"},
            {"id": "YNA", "name": "Natashquan (YNA)"},
            {"id": "CPR", "name": "Natrona Co Intl (CPR)"},
            {"id": "INU", "name": "Nauru Intl (INU)"},
            {"id": "SUV", "name": "Nausori Intl (SUV)"},
            {"id": "NBU", "name": "Naval Air Station (NBU)"},
            {"id": "NVI", "name": "Navoi Airport (NVI)"},
            {"id": "WNS", "name": "Nawabshah (WNS)"},
            {"id": "JNX", "name": "Naxos (JNX)"},
            {"id": "ELA", "name": "Naypyidaw (ELA)"},
            {"id": "NYT", "name": "NAYPYITAW (NYT)"},
            {"id": "NDJ", "name": "Ndjamena Hassan Djamous (NDJ)"},
            {"id": "FIH", "name": "Ndjili Intl (FIH)"},
            {"id": "NLA", "name": "Ndola (NLA)"},
            {"id": "NLO", "name": "Ndolo (NLO)"},
            {"id": "DUU", "name": "Ndutu (DUU)"},
            {"id": "VOL", "name": "Nea Anchialos (VOL)"},
            {"id": "NEC", "name": "Necochea Airport (NEC)"},
            {"id": "CNP", "name": "Neerlerit Inaat Airport (CNP)"},
            {"id": "TOE", "name": "Nefta (TOE)"},
            {"id": "NFG", "name": "Nefteyugansk Airport (NFG)"},
            {"id": "GXG", "name": "Negage (GXG)"},
            {"id": "NEG", "name": "Negril Aerodrome (NEG)"},
            {"id": "EAM", "name": "Nejran (EAM)"},
            {"id": "LSV", "name": "Nellis Afb (LSV)"},
            {"id": "NSN", "name": "Nelson (NSN)"},
            {"id": "NLG", "name": "Nelson Lagoon (NLG)"},
            {"id": "NLP", "name": "Nelspruit Airport (NLP)"},
            {"id": "EMN", "name": "Nema (EMN)"},
            {"id": "YNS", "name": "Nemiscau Airport (YNS)"},
            {"id": "KEP", "name": "Nepalgunj Airport (KEP)"},
            {"id": "CCU", "name": "Netaji Subhash Chandra Bose Intl (CCU)"},
            {"id": "QNC", "name": "Neuchatel Airport (QNC)"},
            {"id": "BRE", "name": "Neuenland (BRE)"},
            {"id": "EUM", "name": "Neumuenster (EUM)"},
            {"id": "EWB", "name": "New Bedford Regional Airport (EWB)"},
            {"id": "CAT", "name": "New Bight Airport (CAT)"},
            {"id": "ZRZ", "name": "New Carrollton Rail Station (ZRZ)"},
            {"id": "ILG", "name": "New Castle (ILG)"},
            {"id": "JCI", "name": "New Century AirCenter Airport (JCI)"},
            {"id": "CTS", "name": "New Chitose (CTS)"},
            {"id": "ZVE", "name": "New Haven Rail Station (ZVE)"},
            {"id": "KKJ", "name": "New Kitakyushu (KKJ)"},
            {"id": "NBG", "name": "New Orleans Nas Jrb (NBG)"},
            {"id": "NPL", "name": "New Plymouth (NPL)"},
            {"id": "KNW", "name": "New Stuyahok Airport (KNW)"},
            {"id": "TNE", "name": "New Tanegashima (TNE)"},
            {"id": "EWR", "name": "Newark Liberty Intl (EWR)"},
            {"id": "ZRP", "name": "Newark Penn Station (ZRP)"},
            {"id": "NCL", "name": "Newcastle (NCL)"},
            {"id": "NCS", "name": "Newcastle (NCS)"},
            {"id": "NTL", "name": "Newcastle Airport (NTL)"},
            {"id": "ZNE", "name": "Newman Airport (ZNE)"},
            {"id": "ONP", "name": "Newport Municipal Airport (ONP)"},
            {"id": "PHF", "name": "Newport News Williamsburg Intl (PHF)"},
            {"id": "UUU", "name": "Newport State (UUU)"},
            {"id": "WWT", "name": "Newtok Airport (WWT)"},
            {"id": "EWK", "name": "Newton City-County Airport (EWK)"},
            {"id": "NEZ", "name": "Nezhitino (NEZ)"},
            {"id": "NGE", "name": "Ngaoundere (NGE)"},
            {"id": "NGI", "name": "Ngau Airport (NGI)"},
            {"id": "IRA", "name": "Ngorangora Airport (IRA)"},
            {"id": "NHA", "name": "Nhatrang (NHA)"},
            {"id": "YCM", "name": "Niagara District (YCM)"},
            {"id": "IAG", "name": "Niagara Falls Intl (IAG)"},
            {"id": "LRL", "name": "Niamtougou International (LRL)"},
            {"id": "NIU", "name": "Niau (NIU)"},
            {"id": "NIC", "name": "Nicosia International Airport (NIC)"},
            {"id": "NRN", "name": "Niederrhein (NRN)"},
            {"id": "NME", "name": "Nightmute Airport (NME)"},
            {"id": "KIJ", "name": "Niigata (KIJ)"},
            {"id": "NIB", "name": "Nikolai Airport (NIB)"},
            {"id": "IKO", "name": "Nikolski Air Station (IKO)"},
            {"id": "HER", "name": "Nikos Kazantzakis (HER)"},
            {"id": "NIG", "name": "Nikunau Airport (NIG)"},
            {"id": "MNL", "name": "Ninoy Aquino Intl (MNL)"},
            {"id": "NIO", "name": "Nioki Airport (NIO)"},
            {"id": "INI", "name": "Nis (INI)"},
            {"id": "IIS", "name": "Nissan Island Airport (IIS)"},
            {"id": "IUE", "name": "Niue International Airport (IUE)"},
            {"id": "NJC", "name": "Nizhnevartovsk (NJC)"},
            {"id": "GOJ", "name": "Nizhny Novgorod (GOJ)"},
            {"id": "ABV", "name": "Nnamdi Azikiwe Intl (ABV)"},
            {"id": "WTK", "name": "Noatak Airport (WTK)"},
            {"id": "NOG", "name": "Nogales Intl (NOG)"},
            {"id": "OLS", "name": "Nogales Intl (OLS)"},
            {"id": "HAN", "name": "Noibai Intl (HAN)"},
            {"id": "OME", "name": "Nome (OME)"},
            {"id": "NNL", "name": "Nondalton Airport (NNL)"},
            {"id": "NON", "name": "Nonouti Airport (NON)"},
            {"id": "NRD", "name": "Norderney (NRD)"},
            {"id": "NOR", "name": "Nordfjordur Airport (NOR)"},
            {"id": "ORF", "name": "Norfolk Intl (ORF)"},
            {"id": "NLK", "name": "Norfolk Island Intl (NLK)"},
            {"id": "NGU", "name": "Norfolk Ns (NGU)"},
            {"id": "KIN", "name": "Norman Manley Intl (KIN)"},
            {"id": "YVQ", "name": "Norman Wells (YVQ)"},
            {"id": "SJC", "name": "Norman Y Mineta San Jose Intl (SJC)"},
            {"id": "NTN", "name": "Normanton Airport (NTN)"},
            {"id": "NUS", "name": "Norsup Airport (NUS)"},
            {"id": "YQW", "name": "North Battleford (YQW)"},
            {"id": "YYB", "name": "North Bay (YYB)"},
            {"id": "NCA", "name": "North Caicos (NCA)"},
            {"id": "SFZ", "name": "North Central State (SFZ)"},
            {"id": "ELH", "name": "North Eleuthera (ELH)"},
            {"id": "NZY", "name": "North Island Nas (NZY)"},
            {"id": "VGT", "name": "North Las Vegas Airport (VGT)"},
            {"id": "HWO", "name": "North Perry (HWO)"},
            {"id": "LBF", "name": "North Platte Regional Airport Lee Bird Field (LBF)"},
            {"id": "NRL", "name": "North Ronaldsay Airport (NRL)"},
            {"id": "YNO", "name": "North Spirit Lake Airport (YNO)"},
            {"id": "OLZ", "name": "North West Santo Airport (OLZ)"},
            {"id": "WWP", "name": "North Whale Seaplane Base (WWP)"},
            {"id": "GAD", "name": "Northeast Alabama Regional Airport (GAD)"},
            {"id": "PNE", "name": "Northeast Philadelphia (PNE)"},
            {"id": "WFK", "name": "Northern Aroostook Regional Airport (WFK)"},
            {"id": "PQI", "name": "Northern Maine Rgnl At Presque Isle (PQI)"},
            {"id": "NHT", "name": "Northolt (NHT)"},
            {"id": "ORT", "name": "Northway (ORT)"},
            {"id": "MSL", "name": "Northwest Alabama Regional Airport (MSL)"},
            {"id": "YNE", "name": "Norway House Airport (YNE)"},
            {"id": "NWI", "name": "Norwich (NWI)"},
            {"id": "OWD", "name": "Norwood Memorial Airport (OWD)"},
            {"id": "NOB", "name": "Nosara (NOB)"},
            {"id": "NSH", "name": "Noshahr Airport (NSH)"},
            {"id": "NTQ", "name": "Noto (NTQ)"},
            {"id": "NTB", "name": "Notodden (NTB)"},
            {"id": "NQT", "name": "Nottingham Airport (NQT)"},
            {"id": "EMA", "name": "Nottingham East Midlands (EMA)"},
            {"id": "NDB", "name": "Nouadhibou (NDB)"},
            {"id": "NKC", "name": "Nouakchott (NKC)"},
            {"id": "GHA", "name": "Noumerat (GHA)"},
            {"id": "NVP", "name": "Novo Aripuana Airport (NVP)"},
            {"id": "KHV", "name": "Novy (KHV)"},
            {"id": "NUX", "name": "Novyi Urengoy (NUX)"},
            {"id": "NOA", "name": "Nowra Airport (NOA)"},
            {"id": "NOJ", "name": "Noyabrsk (NOJ)"},
            {"id": "ZAQ", "name": "Nuernberg Railway (ZAQ)"},
            {"id": "STY", "name": "Nueva Hesperides Intl (STY)"},
            {"id": "LGQ", "name": "Nueva Loja Airport (LGQ)"},
            {"id": "NUI", "name": "Nuiqsut Airport (NUI)"},
            {"id": "UKU", "name": "Nuku Airport (UKU)"},
            {"id": "NHV", "name": "Nuku Hiva (NHV)"},
            {"id": "NCU", "name": "Nukus Airport (NCU)"},
            {"id": "NUL", "name": "Nulato Airport (NUL)"},
            {"id": "NUP", "name": "Nunapitchuk Airport (NUP)"},
            {"id": "NNX", "name": "Nunukan Airport (NNX)"},
            {"id": "NUE", "name": "Nurnberg (NUE)"},
            {"id": "NUR", "name": "Nurnberg HBF (NUR)"},
            {"id": "GZO", "name": "Nusatupe Airport (GZO)"},
            {"id": "GOH", "name": "Nuuk (GOH)"},
            {"id": "XNA", "name": "NW Arkansas Regional (XNA)"},
            {"id": "NYA", "name": "Nyagan Airport (NYA)"},
            {"id": "UYL", "name": "Nyala Airport (UYL)"},
            {"id": "NYE", "name": "NYERI (NYE)"},
            {"id": "LZY", "name": "Nyingchi Airport (LZY)"},
            {"id": "NYN", "name": "Nynashamn Ferry Port (NYN)"},
            {"id": "O27", "name": "Oakdale Airport (O27)"},
            {"id": "OKY", "name": "Oakey Airport (OKY)"},
            {"id": "PTK", "name": "Oakland Co. Intl (PTK)"},
            {"id": "OAM", "name": "Oamaru (OAM)"},
            {"id": "OBN", "name": "Oban Airport (OBN)"},
            {"id": "PDA", "name": "Obando Airport (PDA)"},
            {"id": "OBF", "name": "Oberpfaffenhofen (OBF)"},
            {"id": "OBO", "name": "Obihiro (OBO)"},
            {"id": "OBX", "name": "Obo Airport (OBX)"},
            {"id": "OBC", "name": "Obock (OBC)"},
            {"id": "60J", "name": "Ocean Isle Beach Airport (60J)"},
            {"id": "E55", "name": "Ocean Ridge Airport (E55)"},
            {"id": "W04", "name": "Ocean Shores Municipal (W04)"},
            {"id": "NTU", "name": "Oceana Nas (NTU)"},
            {"id": "L52", "name": "Oceano County Airport (L52)"},
            {"id": "LEH", "name": "Octeville (LEH)"},
            {"id": "ONJ", "name": "Odate Noshiro Airport (ONJ)"},
            {"id": "ODE", "name": "Odense (ODE)"},
            {"id": "ODS", "name": "Odesa Intl (ODS)"},
            {"id": "ODH", "name": "Odiham (ODH)"},
            {"id": "OFF", "name": "Offutt Afb (OFF)"},
            {"id": "OFU", "name": "Ofu Airport (OFU)"},
            {"id": "OGS", "name": "Ogdensburg Intl (OGS)"},
            {"id": "OGL", "name": "Ogle (OGL)"},
            {"id": "YOG", "name": "Ogoki Post Airport (YOG)"},
            {"id": "GZT", "name": "Oguzeli (GZT)"},
            {"id": "OSU", "name": "Ohio State University Airport (OSU)"},
            {"id": "OHD", "name": "Ohrid (OHD)"},
            {"id": "OIT", "name": "Oita (OIT)"},
            {"id": "OKD", "name": "Okadama Airport (OKD)"},
            {"id": "OKF", "name": "Okaukuejo Airport (OKF)"},
            {"id": "OKJ", "name": "Okayama (OKJ)"},
            {"id": "WAW", "name": "Okecie (WAW)"},
            {"id": "OHH", "name": "Okha Airport (OHH)"},
            {"id": "OHO", "name": "Okhotsk Airport (OHO)"},
            {"id": "OKI", "name": "Oki (OKI)"},
            {"id": "OKN", "name": "Okondja (OKN)"},
            {"id": "OIR", "name": "Okushiri (OIR)"},
            {"id": "EOH", "name": "Olaya Herrera (EOH)"},
            {"id": "OLB", "name": "Olbia Costa Smeralda (OLB)"},
            {"id": "YOC", "name": "Old Crow (YOC)"},
            {"id": "OLH", "name": "Old Harbor Airport (OLH)"},
            {"id": "KOY", "name": "Olga Bay Seaplane Base (KOY)"},
            {"id": "ULG", "name": "Olgii Airport (ULG)"},
            {"id": "OLV", "name": "Olive Branch Muni (OLV)"},
            {"id": "OLM", "name": "Olympia Regional Airpor (OLM)"},
            {"id": "OLP", "name": "Olympic Dam Airport (OLP)"},
            {"id": "OMB", "name": "Omboue Hopital (OMB)"},
            {"id": "OMS", "name": "Omsk (OMS)"},
            {"id": "OND", "name": "Ondangwa Airport (OND)"},
            {"id": "VPE", "name": "Ondjiva Pereira Airport (VPE)"},
            {"id": "ONH", "name": "Oneonta Municipal Airport (ONH)"},
            {"id": "ONB", "name": "Ononge Airport (ONB)"},
            {"id": "ONS", "name": "Onslow  (ONS)"},
            {"id": "ONT", "name": "Ontario Intl (ONT)"},
            {"id": "OST", "name": "Oostende (OST)"},
            {"id": "OPF", "name": "Opa Locka (OPF)"},
            {"id": "OMR", "name": "Oradea (OMR)"},
            {"id": "ORA", "name": "Oran (ORA)"},
            {"id": "OAG", "name": "Orange Airport (OAG)"},
            {"id": "MGJ", "name": "Orange County Airport (MGJ)"},
            {"id": "OMD", "name": "Oranjemund Airport (OMD)"},
            {"id": "ORP", "name": "Orapa Airport (ORP)"},
            {"id": "ESD", "name": "Orcas Island Airport (ESD)"},
            {"id": "OKB", "name": "Orchid Beach (OKB)"},
            {"id": "DSN", "name": "Ordos Ejin Horo (DSN)"},
            {"id": "ORB", "name": "Orebro (ORB)"},
            {"id": "REN", "name": "Orenburg (REN)"},
            {"id": "MOA", "name": "Orestes Acosta (MOA)"},
            {"id": "ORJ", "name": "Orinduik Airport (ORJ)"},
            {"id": "ORX", "name": "Oriximina Airport (ORX)"},
            {"id": "OLA", "name": "Orland (OLA)"},
            {"id": "DWS", "name": "Orlando (DWS)"},
            {"id": "JDO", "name": "Orlando Bezerra de Menezes Airport (JDO)"},
            {"id": "UMU", "name": "Orlando de Carvalho Airport (UMU)"},
            {"id": "MCO", "name": "Orlando Intl (MCO)"},
            {"id": "SFB", "name": "Orlando Sanford Intl (SFB)"},
            {"id": "ORY", "name": "Orly (ORY)"},
            {"id": "ORW", "name": "Ormara Airport (ORW)"},
            {"id": "OMC", "name": "Ormoc Airport (OMC)"},
            {"id": "OMN", "name": "Ormond Beach municipal Airport (OMN)"},
            {"id": "OER", "name": "Ornskoldsvik (OER)"},
            {"id": "ORS", "name": "Orpheus Island (ORS)"},
            {"id": "OSW", "name": "Orsk Airport (OSW)"},
            {"id": "ITM", "name": "Osaka Intl (ITM)"},
            {"id": "OSN", "name": "Osan Ab (OSN)"},
            {"id": "OSC", "name": "Oscoda Wurtsmith (OSC)"},
            {"id": "OSS", "name": "Osh (OSS)"},
            {"id": "YOO", "name": "Oshawa Airport (YOO)"},
            {"id": "OIM", "name": "Oshima (OIM)"},
            {"id": "OSI", "name": "Osijek (OSI)"},
            {"id": "OSK", "name": "Oskarshamn (OSK)"},
            {"id": "ZYL", "name": "Osmany Intl (ZYL)"},
            {"id": "HRK", "name": "Osnova International Airport (HRK)"},
            {"id": "BHF", "name": "Ostbahnhof (BHF)"},
            {"id": "OXB", "name": "Osvaldo Vieira International Airport (OXB)"},
            {"id": "AGV", "name": "Oswaldo Guevara Mujica (AGV)"},
            {"id": "QOT", "name": "Otaru (QOT)"},
            {"id": "FMH", "name": "Otis Angb (FMH)"},
            {"id": "YOW", "name": "Ottawa Macdonald Cartier Intl (YOW)"},
            {"id": "OTU", "name": "Otu (OTU)"},
            {"id": "OUA", "name": "Ouagadougou (OUA)"},
            {"id": "AJN", "name": "Ouani (AJN)"},
            {"id": "OGX", "name": "Ouargla (OGX)"},
            {"id": "OZZ", "name": "Ouarzazate (OZZ)"},
            {"id": "ODY", "name": "Oudomxay (ODY)"},
            {"id": "DUH", "name": "Oudtshoorn (DUH)"},
            {"id": "HME", "name": "Oued Irara (HME)"},
            {"id": "OUE", "name": "Ouesso (OUE)"},
            {"id": "OUL", "name": "Oulu (OUL)"},
            {"id": "OIA", "name": "Ourilandia do Norte Airport (OIA)"},
            {"id": "MAX", "name": "Ouro Sogui Airport (MAX)"},
            {"id": "OUK", "name": "Outer Skerries Airport (OUK)"},
            {"id": "UVE", "name": "Ouvea (UVE)"},
            {"id": "KOZ", "name": "Ouzinkie Airport (KOZ)"},
            {"id": "VDA", "name": "Ovda (VDA)"},
            {"id": "FTX", "name": "Owando (FTX)"},
            {"id": "GCM", "name": "Owen Roberts Intl (GCM)"},
            {"id": "OWB", "name": "Owensboro Daviess County Airport (OWB)"},
            {"id": "YOH", "name": "Oxford House Airport (YOH)"},
            {"id": "OXR", "name": "Oxnard - Ventura County (OXR)"},
            {"id": "OYE", "name": "Oyem (OYE)"},
            {"id": "OZC", "name": "Ozamis (OZC)"},
            {"id": "OZA", "name": "Ozona Muni (OZA)"},
            {"id": "JFR", "name": "Paamiut Heliport (JFR)"},
            {"id": "55S", "name": "Packwood (55S)"},
            {"id": "QQP", "name": "Paddington Station (QQP)"},
            {"id": "PAD", "name": "Paderborn Lippstadt (PAD)"},
            {"id": "QPA", "name": "Padova (QPA)"},
            {"id": "PEM", "name": "Padre Aldamiz (PEM)"},
            {"id": "PFO", "name": "Pafos Intl (PFO)"},
            {"id": "PAG", "name": "Pagadian (PAG)"},
            {"id": "FMY", "name": "Page Fld (FMY)"},
            {"id": "PGA", "name": "Page Municipal Airport (PGA)"},
            {"id": "PPG", "name": "Pago Pago Intl (PPG)"},
            {"id": "PHK", "name": "Pahokee Airport (PHK)"},
            {"id": "PYY", "name": "Pai (PYY)"},
            {"id": "PJA", "name": "Pajala Airport (PJA)"},
            {"id": "PNV", "name": "Pajuostis (PNV)"},
            {"id": "PKK", "name": "Pakhokku Airport (PKK)"},
            {"id": "PKZ", "name": "Pakse (PKZ)"},
            {"id": "PAF", "name": "Pakuba Airport (PAF)"},
            {"id": "PSX", "name": "Palacios Muni (PSX)"},
            {"id": "PLQ", "name": "Palanga Intl (PLQ)"},
            {"id": "PMO", "name": "Palermo (PMO)"},
            {"id": "LNA", "name": "Palm Beach Co Park (LNA)"},
            {"id": "PBI", "name": "Palm Beach Intl (PBI)"},
            {"id": "PMK", "name": "Palm Island Airport (PMK)"},
            {"id": "PSP", "name": "Palm Springs Intl (PSP)"},
            {"id": "PMZ", "name": "Palmar Sur (PMZ)"},
            {"id": "PMW", "name": "Palmas (PMW)"},
            {"id": "PMD", "name": "Palmdale Rgnl Usaf Plt 42 (PMD)"},
            {"id": "PAQ", "name": "Palmer Muni (PAQ)"},
            {"id": "PMR", "name": "Palmerston North (PMR)"},
            {"id": "PMS", "name": "Palmyra (PMS)"},
            {"id": "PAO", "name": "Palo Alto Airport of Santa Clara County (PAO)"},
            {"id": "PVP", "name": "Palo Verde Airport (PVP)"},
            {"id": "BGA", "name": "Palonegro (BGA)"},
            {"id": "PNA", "name": "Pamplona (PNA)"},
            {"id": "BHZ", "name": "Pampulha (BHZ)"},
            {"id": "PLU", "name": "Pampulha Carlos Drummond De Andrade (PLU)"},
            {"id": "PFN", "name": "Panama City Bay Co Intl (PFN)"},
            {"id": "ECP", "name": "Panama City-NW Florida Bea. (ECP)"},
            {"id": "EAT", "name": "Pangborn Field (EAT)"},
            {"id": "YXP", "name": "Pangnirtung (YXP)"},
            {"id": "PSU", "name": "Pangsuma Airport (PSU)"},
            {"id": "PJG", "name": "Panjgur (PJG)"},
            {"id": "PNL", "name": "Pantelleria (PNL)"},
            {"id": "PGH", "name": "Pantnagar (PGH)"},
            {"id": "PZI", "name": "Panzhihua (PZI)"},
            {"id": "PSV", "name": "Papa Stour Airport (PSV)"},
            {"id": "PPW", "name": "Papa Westray Airport (PPW)"},
            {"id": "COE", "name": "Pappy Boyington (COE)"},
            {"id": "PBO", "name": "Paraburdoo Airport (PBO)"},
            {"id": "PAJ", "name": "Parachinar Airport (PAJ)"},
            {"id": "WZY", "name": "Paradise Island Seaplane Base (WZY)"},
            {"id": "PPQ", "name": "Paraparaumu (PPQ)"},
            {"id": "PED", "name": "Pardubice (PED)"},
            {"id": "PKE", "name": "Parkes Airport (PKE)"},
            {"id": "PMF", "name": "Parma (PMF)"},
            {"id": "EPU", "name": "Parnu (EPU)"},
            {"id": "PBH", "name": "Paro (PBH)"},
            {"id": "PAS", "name": "Paros (PAS)"},
            {"id": "YPD", "name": "Parry Sound (YPD)"},
            {"id": "PFQ", "name": "Parsabade Moghan Airport (PFQ)"},
            {"id": "KRR", "name": "Pashkovskiy (KRR)"},
            {"id": "PSI", "name": "Pasni (PSI)"},
            {"id": "AOL", "name": "Paso De Los Libres (AOL)"},
            {"id": "IXP", "name": "Pathankot (IXP)"},
            {"id": "BSX", "name": "Pathein Airport (BSX)"},
            {"id": "PAT", "name": "Patna (PAT)"},
            {"id": "POJ", "name": "Patos de Minas Airport (POJ)"},
            {"id": "PFJ", "name": "Patreksfjordur (PFJ)"},
            {"id": "COF", "name": "Patrick Afb (COF)"},
            {"id": "PAN", "name": "Pattani (PAN)"},
            {"id": "AMQ", "name": "Pattimura (AMQ)"},
            {"id": "NHK", "name": "Patuxent River Nas (NHK)"},
            {"id": "PUF", "name": "Pau Pyrenees (PUF)"},
            {"id": "YPC", "name": "Paulatuk (YPC)"},
            {"id": "FBE", "name": "Paulo Abdala Airport (FBE)"},
            {"id": "PAV", "name": "Paulo Afonso (PAV)"},
            {"id": "PWQ", "name": "Pavlodar (PWQ)"},
            {"id": "QPG", "name": "Paya Lebar (QPG)"},
            {"id": "YPE", "name": "Peace River (YPE)"},
            {"id": "PSM", "name": "Pease International Tradeport (PSM)"},
            {"id": "YPO", "name": "Peawanuck Airport (YPO)"},
            {"id": "PEX", "name": "Pechora (PEX)"},
            {"id": "PEQ", "name": "Pecos Municipal Airport (PEQ)"},
            {"id": "QPJ", "name": "Pecs (QPJ)"},
            {"id": "PDB", "name": "Pedro Bay Airport (PDB)"},
            {"id": "TBP", "name": "Pedro Canga (TBP)"},
            {"id": "PEF", "name": "Peenemunde Airfield (PEF)"},
            {"id": "C23", "name": "Peleliu Airfield (C23)"},
            {"id": "PEC", "name": "Pelican Seaplane Base (PEC)"},
            {"id": "PLN", "name": "Pellston Regional Airport of Emmet County Airport (PLN)"},
            {"id": "PET", "name": "Pelotas (PET)"},
            {"id": "PMA", "name": "Pemba (PMA)"},
            {"id": "POL", "name": "Pemba (POL)"},
            {"id": "PMB", "name": "Pembina Muni (PMB)"},
            {"id": "YTA", "name": "Pembroke Airport (YTA)"},
            {"id": "PEN", "name": "Penang Intl (PEN)"},
            {"id": "PDO", "name": "Pendopo Airport (PDO)"},
            {"id": "CBN", "name": "Penggung (CBN)"},
            {"id": "ZBP", "name": "Penn Station (ZBP)"},
            {"id": "ZYP", "name": "Penn Station (ZYP)"},
            {"id": "PEA", "name": "Penneshaw Airport (PEA)"},
            {"id": "PYE", "name": "Penrhyn Island Airport (PYE)"},
            {"id": "NPA", "name": "Pensacola Nas (NPA)"},
            {"id": "PNS", "name": "Pensacola Rgnl (PNS)"},
            {"id": "YYF", "name": "Penticton (YYF)"},
            {"id": "PEZ", "name": "Penza Airport (PEZ)"},
            {"id": "PZE", "name": "Penzance Heliport (PZE)"},
            {"id": "PIA", "name": "Peoria Regional (PIA)"},
            {"id": "IBE", "name": "Perales (IBE)"},
            {"id": "PMQ", "name": "Perito Moreno (PMQ)"},
            {"id": "40J", "name": "Perry-Foley Airport (40J)"},
            {"id": "KPV", "name": "Perryville Airport (KPV)"},
            {"id": "PGU", "name": "Persian Gulf Airport (PGU)"},
            {"id": "PER", "name": "Perth Intl (PER)"},
            {"id": "JAD", "name": "Perth Jandakot (JAD)"},
            {"id": "PSL", "name": "Perth Scone Airport (PSL)"},
            {"id": "PEG", "name": "Perugia (PEG)"},
            {"id": "PSR", "name": "Pescara (PSR)"},
            {"id": "PEW", "name": "Peshawar Intl (PEW)"},
            {"id": "YWA", "name": "Petawawa (YWA)"},
            {"id": "YPQ", "name": "Peterborough (YPQ)"},
            {"id": "XVH", "name": "Peterborough Railway Station (XVH)"},
            {"id": "PSG", "name": "Petersburg James A. Johnson (PSG)"},
            {"id": "PPK", "name": "Petropavlosk South Airport (PPK)"},
            {"id": "PES", "name": "Petrozavodsk Airport (PES)"},
            {"id": "PWE", "name": "Pevek (PWE)"},
            {"id": "PHW", "name": "Phalaborwa (PHW)"},
            {"id": "PHA", "name": "Phan Rang Airport (PHA)"},
            {"id": "PPL", "name": "Phaplu (PPL)"},
            {"id": "PHY", "name": "Phetchabun (PHY)"},
            {"id": "ZFV", "name": "Philadelphia 30th St Station (ZFV)"},
            {"id": "PHL", "name": "Philadelphia Intl (PHL)"},
            {"id": "MJN", "name": "Philibert Tsiranana (MJN)"},
            {"id": "TOP", "name": "Philip Billard Muni (TOP)"},
            {"id": "BZE", "name": "Philip S W Goldson Intl (BZE)"},
            {"id": "APG", "name": "Phillips Aaf (APG)"},
            {"id": "PHS", "name": "Phitsanulok (PHS)"},
            {"id": "PNH", "name": "Phnom Penh Intl (PNH)"},
            {"id": "SYX", "name": "Phoenix International (SYX)"},
            {"id": "A39", "name": "Phoenix Regional Airport (A39)"},
            {"id": "PHX", "name": "Phoenix Sky Harbor Intl (PHX)"},
            {"id": "AZA", "name": "Phoenix-Mesa Gateway (AZA)"},
            {"id": "PRH", "name": "Phrae (PRH)"},
            {"id": "HUI", "name": "Phu Bai (HUI)"},
            {"id": "UIH", "name": "Phu Cat Airport (UIH)"},
            {"id": "HKT", "name": "Phuket Intl (HKT)"},
            {"id": "QPZ", "name": "Piacenza (QPZ)"},
            {"id": "POS", "name": "Piarco (POS)"},
            {"id": "ZAL", "name": "Pichoy (ZAL)"},
            {"id": "JZP", "name": "Pickens County Airport (JZP)"},
            {"id": "YPL", "name": "Pickle Lake (YPL)"},
            {"id": "PIX", "name": "Pico (PIX)"},
            {"id": "GSO", "name": "Piedmont Triad (GSO)"},
            {"id": "PDS", "name": "Piedras Negras Intl (PDS)"},
            {"id": "YUL", "name": "Pierre Elliott Trudeau Intl (YUL)"},
            {"id": "PIR", "name": "Pierre Regional Airport (PIR)"},
            {"id": "PZY", "name": "Piestany (PZY)"},
            {"id": "PZB", "name": "Pietermaritzburg (PZB)"},
            {"id": "YPM", "name": "Pikangikum Airport (YPM)"},
            {"id": "PBX", "name": "Pike County Airport - Hatcher Field (PBX)"},
            {"id": "NTY", "name": "Pilanesberg Intl (NTY)"},
            {"id": "PIP", "name": "Pilot Point Airport (PIP)"},
            {"id": "PQS", "name": "Pilot Station Airport (PQS)"},
            {"id": "MZJ", "name": "Pinal Airpark (MZJ)"},
            {"id": "DUM", "name": "Pinang Kampai (DUM)"},
            {"id": "PIF", "name": "Pingtung South (PIF)"},
            {"id": "FOR", "name": "Pinto Martins Intl (FOR)"},
            {"id": "PSA", "name": "Pisa (PSA)"},
            {"id": "PIO", "name": "Pisco Intl (PIO)"},
            {"id": "PGV", "name": "Pitt-Greenville Airport (PGV)"},
            {"id": "PIT", "name": "Pittsburgh Intl (PIT)"},
            {"id": "4G0", "name": "Pittsburgh-Monroeville Airport (4G0)"},
            {"id": "OTI", "name": "Pitu (OTI)"},
            {"id": "PLJ", "name": "Placencia Airport (PLJ)"},
            {"id": "TTA", "name": "Plage Blanche (TTA)"},
            {"id": "RRG", "name": "Plaine Corail (RRG)"},
            {"id": "SLW", "name": "Plan De Guadalupe Intl (SLW)"},
            {"id": "JYL", "name": "Plantation Airpark (JYL)"},
            {"id": "PTU", "name": "Platinum (PTU)"},
            {"id": "PBG", "name": "Plattsburgh Intl (PBG)"},
            {"id": "ZLO", "name": "Playa De Oro Intl (ZLO)"},
            {"id": "PCM", "name": "Playa del Carmen Airport (PCM)"},
            {"id": "PLD", "name": "Playa Samara Airport (PLD)"},
            {"id": "PYC", "name": "Playon Chico (PYC)"},
            {"id": "PXU", "name": "Pleiku Airport (PXU)"},
            {"id": "DNR", "name": "Pleurtuit (DNR)"},
            {"id": "MXN", "name": "Ploujean (MXN)"},
            {"id": "PDV", "name": "Plovdiv (PDV)"},
            {"id": "UIP", "name": "Pluguffan (UIP)"},
            {"id": "2B2", "name": "Plum Island Airport (2B2)"},
            {"id": "PLH", "name": "Plymouth (PLH)"},
            {"id": "C65", "name": "Plymouth Municipal Airport (C65)"},
            {"id": "PYM", "name": "Plymouth Municipal Airport (PYM)"},
            {"id": "RBR", "name": "PlÃ¡cido de Castro (RBR)"},
            {"id": "KVX", "name": "Pobedilovo Airport (KVX)"},
            {"id": "PIH", "name": "Pocatello Regional Airport (PIH)"},
            {"id": "POO", "name": "Pocos De Caldas (POO)"},
            {"id": "TGD", "name": "Podgorica (TGD)"},
            {"id": "KPO", "name": "Pohang (KPO)"},
            {"id": "PNI", "name": "Pohnpei Intl (PNI)"},
            {"id": "KPB", "name": "Point Baker Seaplane Base (KPB)"},
            {"id": "PHO", "name": "Point Hope Airport (PHO)"},
            {"id": "PIZ", "name": "Point Lay Lrrs (PIZ)"},
            {"id": "NTD", "name": "Point Mugu Nas (NTD)"},
            {"id": "1RL", "name": "Point Roberts Airpark (1RL)"},
            {"id": "GND", "name": "Point Salines Intl (GND)"},
            {"id": "PNR", "name": "Pointe Noire (PNR)"},
            {"id": "FUT", "name": "Pointe Vele Airport (FUT)"},
            {"id": "YNL", "name": "Points North Landing Airport (YNL)"},
            {"id": "PKR", "name": "Pokhara (PKR)"},
            {"id": "KDZ", "name": "Polgolla Reservoir (KDZ)"},
            {"id": "PYJ", "name": "Poliarny Airport (PYJ)"},
            {"id": "POE", "name": "Polk Aaf (POE)"},
            {"id": "4A4", "name": "Polk County Airport - Cornelius Moore Field (4A4)"},
            {"id": "PTG", "name": "Polokwane International (PTG)"},
            {"id": "MES", "name": "Polonia (MES)"},
            {"id": "PLV", "name": "Poltava (PLV)"},
            {"id": "PUM", "name": "Pomalaa (PUM)"},
            {"id": "PMP", "name": "Pompano Beach Airpark (PMP)"},
            {"id": "PNC", "name": "Ponca City Rgnl (PNC)"},
            {"id": "SLP", "name": "Ponciano Arriaga Intl (SLP)"},
            {"id": "YIO", "name": "Pond Inlet (YIO)"},
            {"id": "PNY", "name": "Pondicherry (PNY)"},
            {"id": "PCB", "name": "Pondok Cabe (PCB)"},
            {"id": "PDL", "name": "Ponta Delgada (PDL)"},
            {"id": "PLL", "name": "Ponta Pelada Airport (PLL)"},
            {"id": "PMG", "name": "Ponta Pora (PMG)"},
            {"id": "POB", "name": "Pope Field (POB)"},
            {"id": "POF", "name": "Poplar Bluff Municipal Airport (POF)"},
            {"id": "YHP", "name": "Poplar Hill Airport (YHP)"},
            {"id": "EIA", "name": "Popondetta (EIA)"},
            {"id": "PBD", "name": "Porbandar (PBD)"},
            {"id": "BIA", "name": "Poretta (BIA)"},
            {"id": "POR", "name": "Pori (POR)"},
            {"id": "EDR", "name": "Pormpuraaw Airport (EDR)"},
            {"id": "PTA", "name": "Port Alsworth Airport (PTA)"},
            {"id": "NOW", "name": "Port Angeles Cgas (NOW)"},
            {"id": "PUG", "name": "Port Augusta Airport (PUG)"},
            {"id": "KPY", "name": "Port Bailey Seaplane Base (KPY)"},
            {"id": "WPB", "name": "Port Bergé Airport (WPB)"},
            {"id": "IXZ", "name": "Port Blair (IXZ)"},
            {"id": "17G", "name": "Port Bucyrus-Crawford County Airport (17G)"},
            {"id": "KPC", "name": "Port Clarence Coast Guard Station (KPC)"},
            {"id": "CMH", "name": "Port Columbus Intl (CMH)"},
            {"id": "PLZ", "name": "Port Elizabeth Intl (PLZ)"},
            {"id": "POG", "name": "Port Gentil (POG)"},
            {"id": "PHC", "name": "Port Harcourt Intl (PHC)"},
            {"id": "YZT", "name": "Port Hardy (YZT)"},
            {"id": "PHE", "name": "Port Hedland Intl (PHE)"},
            {"id": "PTH", "name": "Port Heiden Airport (PTH)"},
            {"id": "YHA", "name": "Port Hope Simpson Airport (YHA)"},
            {"id": "PLO", "name": "Port Lincoln Airport (PLO)"},
            {"id": "ORI", "name": "Port Lions Airport (ORI)"},
            {"id": "PQQ", "name": "Port Macquarie Airport (PQQ)"},
            {"id": "YPN", "name": "Port Menier (YPN)"},
            {"id": "PML", "name": "Port Moller Airport (PML)"},
            {"id": "POM", "name": "Port Moresby Jacksons Intl (POM)"},
            {"id": "PRL", "name": "Port Oceanic Airport (PRL)"},
            {"id": "BE2", "name": "Port of Belfast (BE2)"},
            {"id": "S46", "name": "Port O'Connor Airfield (S46)"},
            {"id": "PPV", "name": "Port Protection Seaplane Base (PPV)"},
            {"id": "PSD", "name": "Port Said (PSD)"},
            {"id": "PZU", "name": "Port Sudan New International Airport (PZU)"},
            {"id": "VLI", "name": "Port Vila Bauerfield (VLI)"},
            {"id": "KPR", "name": "Port Williams Seaplane Base (KPR)"},
            {"id": "PAX", "name": "Port-de-Paix Airport (PAX)"},
            {"id": "C47", "name": "Portage Municipal Airport (C47)"},
            {"id": "NPZ", "name": "Porter County Municipal Airport (NPZ)"},
            {"id": "PTJ", "name": "Portland Airport (PTJ)"},
            {"id": "HIO", "name": "Portland Hillsboro (HIO)"},
            {"id": "PDX", "name": "Portland Intl (PDX)"},
            {"id": "PWM", "name": "Portland Intl Jetport (PWM)"},
            {"id": "TTD", "name": "Portland Troutdale (TTD)"},
            {"id": "OPO", "name": "Porto (OPO)"},
            {"id": "PBN", "name": "Porto Amboim (PBN)"},
            {"id": "PNB", "name": "Porto Nacional (PNB)"},
            {"id": "PXO", "name": "Porto Santo (PXO)"},
            {"id": "POW", "name": "Portoroz (POW)"},
            {"id": "PME", "name": "Portsmouth Airport (PME)"},
            {"id": "PSS", "name": "Posadas (PSS)"},
            {"id": "YSO", "name": "Postville Airport (YSO)"},
            {"id": "YPW", "name": "Powell River Airport (YPW)"},
            {"id": "XYG", "name": "Praha hlavni nadrazi (XYG)"},
            {"id": "RAI", "name": "Praia International Airport (RAI)"},
            {"id": "PRI", "name": "Praslin (PRI)"},
            {"id": "IMP", "name": "Prefeito Renato Moreira (IMP)"},
            {"id": "SNE", "name": "Preguica (SNE)"},
            {"id": "PRV", "name": "Prerov (PRV)"},
            {"id": "JPA", "name": "Presidente Castro Pinto (JPA)"},
            {"id": "CPV", "name": "Presidente Joao Suassuna (CPV)"},
            {"id": "BSB", "name": "Presidente Juscelino Kubitschek (BSB)"},
            {"id": "DIL", "name": "Presidente Nicolau Lobato Intl (DIL)"},
            {"id": "NQN", "name": "Presidente Peron (NQN)"},
            {"id": "PPB", "name": "Presidente Prudente (PPB)"},
            {"id": "PIK", "name": "Prestwick (PIK)"},
            {"id": "ULH", "name": "Prince Abdul Majeed Airport (ULH)"},
            {"id": "YPA", "name": "Prince Albert Glass Field (YPA)"},
            {"id": "YXS", "name": "Prince George (YXS)"},
            {"id": "ULD", "name": "Prince Mangosuthu Buthelezi (ULD)"},
            {"id": "MED", "name": "Prince Mohammad Bin Abdulaziz (MED)"},
            {"id": "YPR", "name": "Prince Rupert (YPR)"},
            {"id": "HAH", "name": "Prince Said Ibrahim (HAH)"},
            {"id": "SXM", "name": "Princess Juliana Intl (SXM)"},
            {"id": "YDC", "name": "Princeton (YDC)"},
            {"id": "PNM", "name": "Princeton Muni (PNM)"},
            {"id": "PCP", "name": "Principe (PCP)"},
            {"id": "PRN", "name": "Pristina (PRN)"},
            {"id": "SJK", "name": "Professor Urbano Ernesto Stumpf (SJK)"},
            {"id": "PPP", "name": "Proserpine Whitsunday Coast (PPP)"},
            {"id": "PPC", "name": "Prospect Creek Airport (PPC)"},
            {"id": "S40", "name": "Prosser (S40)"},
            {"id": "MRS", "name": "Provence (MRS)"},
            {"id": "PLS", "name": "Providenciales (PLS)"},
            {"id": "PVS", "name": "Provideniya Bay (PVS)"},
            {"id": "PVC", "name": "Provincetown Muni (PVC)"},
            {"id": "PVU", "name": "Provo Municipal Airport (PVU)"},
            {"id": "ZPC", "name": "Pucón Airport (ZPC)"},
            {"id": "PVG", "name": "Pudong (PVG)"},
            {"id": "PUB", "name": "Pueblo Memorial (PUB)"},
            {"id": "PBR", "name": "Puerto Barrios Airport (PBR)"},
            {"id": "PUZ", "name": "Puerto Cabezas (PUZ)"},
            {"id": "PCR", "name": "Puerto Carreno (PCR)"},
            {"id": "PUD", "name": "Puerto Deseado (PUD)"},
            {"id": "PXM", "name": "Puerto Escondido Intl (PXM)"},
            {"id": "PJM", "name": "Puerto Jimenez Airport (PJM)"},
            {"id": "PEU", "name": "Puerto Lempira Airport (PEU)"},
            {"id": "PNT", "name": "Puerto Natales (PNT)"},
            {"id": "PUE", "name": "Puerto Obaldia (PUE)"},
            {"id": "PPE", "name": "Puerto Penasco (PPE)"},
            {"id": "PPS", "name": "Puerto Princesa (PPS)"},
            {"id": "PUR", "name": "Puerto Rico Airport (PUR)"},
            {"id": "PKP", "name": "Puka Puka (PKP)"},
            {"id": "PUY", "name": "Pula (PUY)"},
            {"id": "PKG", "name": "Pulau Pangkor Airport (PKG)"},
            {"id": "TOD", "name": "Pulau Tioman (TOD)"},
            {"id": "LED", "name": "Pulkovo (LED)"},
            {"id": "PUW", "name": "Pullman-Moscow Rgnl (PUW)"},
            {"id": "PNQ", "name": "Pune (PNQ)"},
            {"id": "PUJ", "name": "Punta Cana Intl (PUJ)"},
            {"id": "PND", "name": "Punta Gorda Airport (PND)"},
            {"id": "LAF", "name": "Purude University Airport (LAF)"},
            {"id": "3W2", "name": "Put-in-Bay Airport (3W2)"},
            {"id": "PBU", "name": "Putao (PBU)"},
            {"id": "4I7", "name": "Putnam County Airport (4I7)"},
            {"id": "QUA", "name": "Puttgarden (QUA)"},
            {"id": "YPX", "name": "Puvirnituq Airport (YPX)"},
            {"id": "FNJ", "name": "Pyongyang Intl (FNJ)"},
            {"id": "KTW", "name": "Pyrzowice (KTW)"},
            {"id": "PEV", "name": "Pécs-Pogány Airport (PEV)"},
            {"id": "NAQ", "name": "Qaanaaq Airport (NAQ)"},
            {"id": "JQA", "name": "Qaarsut Airport (JQA)"},
            {"id": "GBB", "name": "Qabala Airport (GBB)"},
            {"id": "AQI", "name": "Qaisumah (AQI)"},
            {"id": "BPX", "name": "Qamdo Bangda Airport (BPX)"},
            {"id": "JJU", "name": "Qaqortoq Heliport (JJU)"},
            {"id": "JCH", "name": "Qasigiannguit (JCH)"},
            {"id": "JGO", "name": "Qeqertarsuaq Heliport (JGO)"},
            {"id": "IQM", "name": "Qiemo Airport (IQM)"},
            {"id": "YVM", "name": "Qikiqtarjuaq (YVM)"},
            {"id": "XIC", "name": "Qingshan (XIC)"},
            {"id": "IQN", "name": "Qingyang Airport (IQN)"},
            {"id": "NDG", "name": "Qiqihar Sanjiazi Airport (NDG)"},
            {"id": "MLI", "name": "Quad City Intl (MLI)"},
            {"id": "XQU", "name": "Qualicum Beach Airport (XQU)"},
            {"id": "NYG", "name": "Quantico Mcaf (NYG)"},
            {"id": "JJN", "name": "Quanzhou Airport (JJN)"},
            {"id": "YQC", "name": "Quaqtaq Airport (YQC)"},
            {"id": "YQB", "name": "Quebec Jean Lesage Intl (YQB)"},
            {"id": "AMM", "name": "Queen Alia Intl (AMM)"},
            {"id": "GLQ", "name": "Queen Street Station (GLQ)"},
            {"id": "UTW", "name": "Queenstown (UTW)"},
            {"id": "ZQN", "name": "Queenstown (ZQN)"},
            {"id": "UEL", "name": "Quelimane (UEL)"},
            {"id": "QRO", "name": "Queretaro Intercontinental (QRO)"},
            {"id": "YQZ", "name": "Quesnel (YQZ)"},
            {"id": "UET", "name": "Quetta (UET)"},
            {"id": "NLD", "name": "Quetzalcoatl Intl (NLD)"},
            {"id": "AAZ", "name": "Quezaltenango Airport (AAZ)"},
            {"id": "ULP", "name": "Quilpie Airport (ULP)"},
            {"id": "2J9", "name": "Quincy Municipal Airport (2J9)"},
            {"id": "UIN", "name": "Quincy Regional Baldwin Field (UIN)"},
            {"id": "KWN", "name": "Quinhagak Airport (KWN)"},
            {"id": "OQU", "name": "Quonset State Airport (OQU)"},
            {"id": "JUZ", "name": "Quzhou Airport (JUZ)"},
            {"id": "BVC", "name": "Rabil (BVC)"},
            {"id": "VKG", "name": "Rach Gia (VKG)"},
            {"id": "TKG", "name": "Radin Inten II (Branti) Airport (TKG)"},
            {"id": "QXR", "name": "RADOM (QXR)"},
            {"id": "RAT", "name": "Raduzhny Airport (RAT)"},
            {"id": "YRA", "name": "Rae Lakes Airport (YRA)"},
            {"id": "GER", "name": "Rafael Cabrera (GER)"},
            {"id": "BQN", "name": "Rafael Hernandez (BQN)"},
            {"id": "CTG", "name": "Rafael Nunez (CTG)"},
            {"id": "RAH", "name": "Rafha (RAH)"},
            {"id": "BEY", "name": "Rafic Hariri Intl (BEY)"},
            {"id": "RJN", "name": "Rafsanjan Airport (RJN)"},
            {"id": "KTG", "name": "Rahadi Usman (KTG)"},
            {"id": "RFP", "name": "Raiatea (RFP)"},
            {"id": "YOP", "name": "Rainbow Lake Airport (YOP)"},
            {"id": "RPR", "name": "Raipur (RPR)"},
            {"id": "RVV", "name": "Raivavae Airport (RVV)"},
            {"id": "RJA", "name": "Rajahmundry (RJA)"},
            {"id": "RAJ", "name": "Rajkot (RAJ)"},
            {"id": "BKW", "name": "Raleigh County Memorial Airport (BKW)"},
            {"id": "RDU", "name": "Raleigh Durham Intl (RDU)"},
            {"id": "OTZ", "name": "Ralph Wien Mem (OTZ)"},
            {"id": "RBV", "name": "Ramata Airport (RBV)"},
            {"id": "RHP", "name": "Ramechhap (RHP)"},
            {"id": "RAM", "name": "Ramingining Airport (RAM)"},
            {"id": "RNM", "name": "Ramona Airport (RNM)"},
            {"id": "RMP", "name": "Rampart Airport (RMP)"},
            {"id": "RZR", "name": "Ramsar (RZR)"},
            {"id": "RMS", "name": "Ramstein Ab (RMS)"},
            {"id": "NTX", "name": "Ranai Airport (NTX)"},
            {"id": "RIU", "name": "Rancho Murieta (RIU)"},
            {"id": "QRA", "name": "Rand Airport (QRA)"},
            {"id": "06N", "name": "Randall Airport (06N)"},
            {"id": "RND", "name": "Randolph Afb (RND)"},
            {"id": "RGI", "name": "Rangiroa (RGI)"},
            {"id": "YRT", "name": "Rankin Inlet (YRT)"},
            {"id": "UNN", "name": "Ranong (UNN)"},
            {"id": "RAP", "name": "Rapid City Regional Airport (RAP)"},
            {"id": "RAR", "name": "Rarotonga Intl (RAR)"},
            {"id": "RKT", "name": "Ras Al Khaimah Intl (RKT)"},
            {"id": "RAS", "name": "Rasht (RAS)"},
            {"id": "RBE", "name": "Ratanakiri (RBE)"},
            {"id": "RVT", "name": "Ravensthorpe Airport (RVT)"},
            {"id": "RAZ", "name": "Rawalakot (RAZ)"},
            {"id": "RWL", "name": "Rawlins Municipal Airport-Harvey Field (RWL)"},
            {"id": "RDG", "name": "Reading Regional Carl A Spaatz Field (RDG)"},
            {"id": "PVO", "name": "Reales Tamarindos (PVO)"},
            {"id": "REA", "name": "Reao (REA)"},
            {"id": "YQF", "name": "Red Deer Regional (YQF)"},
            {"id": "RDV", "name": "Red Devil Airport (RDV)"},
            {"id": "YRL", "name": "Red Lake Airport (YRL)"},
            {"id": "YRS", "name": "Red Sucker Lake Airport (YRS)"},
            {"id": "RDN", "name": "Redang (RDN)"},
            {"id": "RCL", "name": "Redcliffe Airport (RCL)"},
            {"id": "RDD", "name": "Redding Muni (RDD)"},
            {"id": "RDC", "name": "Redencao Airport (RDC)"},
            {"id": "KRH", "name": "Redhill Aerodrome (KRH)"},
            {"id": "REI", "name": "Redlands Municipal Airport (REI)"},
            {"id": "HUA", "name": "Redstone Aaf (HUA)"},
            {"id": "OSP", "name": "Redzikowo (OSP)"},
            {"id": "CHE", "name": "Reeroe Airport (CHE)"},
            {"id": "RGB", "name": "Regensburg HBF (RGB)"},
            {"id": "REG", "name": "Reggio Calabria (REG)"},
            {"id": "YQR", "name": "Regina Intl (YQR)"},
            {"id": "SEF", "name": "Regional - Hendricks AAF (SEF)"},
            {"id": "JOT", "name": "Regional Airport (JOT)"},
            {"id": "SVH", "name": "Regional Airport (SVH)"},
            {"id": "MGF", "name": "Regional De Maringa Silvio Name Junior (MGF)"},
            {"id": "AUA", "name": "Reina Beatrix Intl (AUA)"},
            {"id": "RNE", "name": "Renaison (RNE)"},
            {"id": "MKW", "name": "Rendani (MKW)"},
            {"id": "YHF", "name": "Rene Fontaine (YHF)"},
            {"id": "RMK", "name": "Renmark (RMK)"},
            {"id": "RNL", "name": "Rennell/Tingoa Airport (RNL)"},
            {"id": "GLD", "name": "Renner Fld (GLD)"},
            {"id": "RNO", "name": "Reno Tahoe Intl (RNO)"},
            {"id": "RNT", "name": "Renton (RNT)"},
            {"id": "YUT", "name": "Repulse Bay (YUT)"},
            {"id": "RES", "name": "Resistencia (RES)"},
            {"id": "YRB", "name": "Resolute Bay (YRB)"},
            {"id": "REU", "name": "Reus (REU)"},
            {"id": "YRV", "name": "Revelstoke Airport (YRV)"},
            {"id": "REY", "name": "Reyes Airport (REY)"},
            {"id": "NQU", "name": "Reyes Murillo Airport (NQU)"},
            {"id": "MVA", "name": "Reykjahlid Airport (MVA)"},
            {"id": "RKV", "name": "Reykjavik (RKV)"},
            {"id": "JXN", "name": "Reynolds Field (JXN)"},
            {"id": "ZPQ", "name": "Rheine Bentlage (ZPQ)"},
            {"id": "RHI", "name": "Rhinelander Oneida County Airport (RHI)"},
            {"id": "RHO", "name": "Rhodes Diagoras (RHO)"},
            {"id": "ESR", "name": "Ricardo García Posada Airport (ESR)"},
            {"id": "RMG", "name": "Richard B Russell Airport (RMG)"},
            {"id": "RVS", "name": "Richard Lloyd Jones Jr Airport (RVS)"},
            {"id": "RCB", "name": "Richards Bay (RCB)"},
            {"id": "RIF", "name": "Richfield Minicipal Airport (RIF)"},
            {"id": "93C", "name": "Richland Airport (93C)"},
            {"id": "RCM", "name": "Richmond (RCM)"},
            {"id": "RCZ", "name": "Richmond County Airport (RCZ)"},
            {"id": "RIC", "name": "Richmond Intl (RIC)"},
            {"id": "RID", "name": "Richmond Municipal Airport (RID)"},
            {"id": "AMA", "name": "Rick Husband Amarillo Intl (AMA)"},
            {"id": "LCK", "name": "Rickenbacker Intl (LCK)"},
            {"id": "3J1", "name": "Ridgeland Airport (3J1)"},
            {"id": "RIX", "name": "Riga Intl (RIX)"},
            {"id": "YRG", "name": "Rigolet Airport (YRG)"},
            {"id": "RJK", "name": "Rijeka (RJK)"},
            {"id": "RMT", "name": "Rimatara (RMT)"},
            {"id": "RMI", "name": "Rimini (RMI)"},
            {"id": "YXK", "name": "Rimouski Airport (YXK)"},
            {"id": "RDS", "name": "Rincon de los Sauces (RDS)"},
            {"id": "RIN", "name": "Ringi Cove Airport (RIN)"},
            {"id": "RCU", "name": "Rio Cuarto Area De Material (RCU)"},
            {"id": "RGL", "name": "Rio Gallegos (RGL)"},
            {"id": "RGA", "name": "Rio Grande (RGA)"},
            {"id": "RIG", "name": "Rio Grande (RIG)"},
            {"id": "RSI", "name": "Rio Sidra (RSI)"},
            {"id": "RYO", "name": "Rio Turbio (RYO)"},
            {"id": "RIS", "name": "Rishiri (RIS)"},
            {"id": "RVY", "name": "Rivera International Airport (RVY)"},
            {"id": "RAL", "name": "Riverside Muni (RAL)"},
            {"id": "RIW", "name": "Riverton Regional (RIW)"},
            {"id": "PGF", "name": "Rivesaltes (PGF)"},
            {"id": "YRI", "name": "Riviere Du Loup (YRI)"},
            {"id": "YTM", "name": "Riviere Rouge - Mont-Tremblant International Inc. Airport (YTM)"},
            {"id": "RWN", "name": "Rivne International Airport (RWN)"},
            {"id": "RIY", "name": "Riyan (RIY)"},
            {"id": "ROA", "name": "Roanoke Regional (ROA)"},
            {"id": "GOB", "name": "Robe Airport (GOB)"},
            {"id": "ORV", "name": "Robert Curtis Memorial Airport (ORV)"},
            {"id": "GRK", "name": "Robert Gray Aaf (GRK)"},
            {"id": "SKB", "name": "Robert L Bradshaw (SKB)"},
            {"id": "RDM", "name": "Roberts Fld (RDM)"},
            {"id": "4B8", "name": "Robertson Field (4B8)"},
            {"id": "YRJ", "name": "Roberval (YRJ)"},
            {"id": "DSA", "name": "Robin Hood Doncaster Sheffield Airport (DSA)"},
            {"id": "WRB", "name": "Robins Afb (WRB)"},
            {"id": "RBM", "name": "Robinson Aaf (RBM)"},
            {"id": "CAY", "name": "Rochambeau (CAY)"},
            {"id": "RCE", "name": "Roche Harbor Seaplane Base (RCE)"},
            {"id": "RST", "name": "Rochester (RST)"},
            {"id": "RCS", "name": "Rochester Airport (RCS)"},
            {"id": "9G1", "name": "Rock Airport (9G1)"},
            {"id": "RKH", "name": "Rock Hill York Co Bryant Airport (RKH)"},
            {"id": "RSD", "name": "Rock Sound (RSD)"},
            {"id": "RKS", "name": "Rock Springs Sweetwater County Airport (RKS)"},
            {"id": "ROK", "name": "Rockhampton (ROK)"},
            {"id": "RWI", "name": "Rocky Mount Wilson Regional Airport (RWI)"},
            {"id": "YRM", "name": "Rocky Mountain House (YRM)"},
            {"id": "BJC", "name": "Rocky Mountain Metropolitan Airport (BJC)"},
            {"id": "ROD", "name": "Rodby Port (ROD)"},
            {"id": "AQP", "name": "Rodriguez Ballon (AQP)"},
            {"id": "MFR", "name": "Rogue Valley Intl Medford (MFR)"},
            {"id": "ROI", "name": "Roi Et (ROI)"},
            {"id": "RMA", "name": "Roma Airport (RMA)"},
            {"id": "TBH", "name": "Romblon Airport (TBH)"},
            {"id": "DCA", "name": "Ronald Reagan Washington Natl (DCA)"},
            {"id": "TRS", "name": "Ronchi Dei Legionari (TRS)"},
            {"id": "RRA", "name": "Ronda Airport (RRA)"},
            {"id": "ROO", "name": "Rondonopolis Airport (ROO)"},
            {"id": "RNP", "name": "Rongelap Island Airport (RNP)"},
            {"id": "RNB", "name": "Ronneby (RNB)"},
            {"id": "RPB", "name": "Roper Bar (RPB)"},
            {"id": "RRS", "name": "Roros (RRS)"},
            {"id": "ROS", "name": "Rosario (ROS)"},
            {"id": "RSJ", "name": "Rosario Seaplane Base (RSJ)"},
            {"id": "TJM", "name": "Roschino (TJM)"},
            {"id": "HTL", "name": "Roscommon Co (HTL)"},
            {"id": "STJ", "name": "Rosecrans Mem (STJ)"},
            {"id": "RFS", "name": "Rosita Airport (RFS)"},
            {"id": "RKE", "name": "Roskilde (RKE)"},
            {"id": "ROV", "name": "Rostov Na Donu (ROV)"},
            {"id": "ROW", "name": "Roswell Intl Air Center (ROW)"},
            {"id": "ROP", "name": "Rota Intl (ROP)"},
            {"id": "ROT", "name": "Rotorua (ROT)"},
            {"id": "RTM", "name": "Rotterdam (RTM)"},
            {"id": "RTA", "name": "Rotuma Airport (RTA)"},
            {"id": "EGC", "name": "Roumaniere (EGC)"},
            {"id": "ZRJ", "name": "Round Lake (Weagamow Lake) Airport (ZRJ)"},
            {"id": "RRK", "name": "Rourkela (RRK)"},
            {"id": "YUY", "name": "Rouyn Noranda (YUY)"},
            {"id": "RVN", "name": "Rovaniemi (RVN)"},
            {"id": "RXS", "name": "Roxas Airport (RXS)"},
            {"id": "URG", "name": "Rubem Berta (URG)"},
            {"id": "RBY", "name": "Ruby Airport (RBY)"},
            {"id": "CZW", "name": "RUDNIKI  (CZW)"},
            {"id": "RUK", "name": "Rukumkot (RUK)"},
            {"id": "RBX", "name": "Rumbek Airport (RBX)"},
            {"id": "RUM", "name": "Rumjatar (RUM)"},
            {"id": "NDU", "name": "Rundu (NDU)"},
            {"id": "RBQ", "name": "Rurrenabaque Airport (RBQ)"},
            {"id": "RUR", "name": "Rurutu (RUR)"},
            {"id": "RSH", "name": "Russian Mission Airport (RSH)"},
            {"id": "RUT", "name": "Rutland State Airport (RUT)"},
            {"id": "PRG", "name": "Ruzyne (PRG)"},
            {"id": "SZS", "name": "Ryans Creek Aerodrome (SZS)"},
            {"id": "RVK", "name": "Ryum Airport (RVK)"},
            {"id": "MQN", "name": "Røssvoll Airport (MQN)"},
            {"id": "RET", "name": "Røst Airport (RET)"},
            {"id": "LPI", "name": "Saab (LPI)"},
            {"id": "SCN", "name": "Saarbrucken (SCN)"},
            {"id": "QSA", "name": "Sabadell Airport (QSA)"},
            {"id": "K83", "name": "Sabetha Municipal (K83)"},
            {"id": "GSS", "name": "Sabi Sabi Airport (GSS)"},
            {"id": "SAW", "name": "Sabiha Gokcen (SAW)"},
            {"id": "AFZ", "name": "Sabzevar National Airport (AFZ)"},
            {"id": "HIN", "name": "Sacheon Air Base (HIN)"},
            {"id": "ZPB", "name": "Sachigo Lake Airport (ZPB)"},
            {"id": "YSY", "name": "Sachs Harbour (YSY)"},
            {"id": "SAC", "name": "Sacramento Executive (SAC)"},
            {"id": "SMF", "name": "Sacramento Intl (SMF)"},
            {"id": "MHR", "name": "Sacramento Mather (MHR)"},
            {"id": "SKO", "name": "Sadiq Abubakar Iii Intl (SKO)"},
            {"id": "SAD", "name": "Safford Regional Airport (SAD)"},
            {"id": "HSG", "name": "Saga Airport (HSG)"},
            {"id": "ACP", "name": "Sahand Airport (ACP)"},
            {"id": "SBR", "name": "Saibai Island Airport (SBR)"},
            {"id": "SPD", "name": "Saidpur (SPD)"},
            {"id": "SDT", "name": "Saidu Sharif (SDT)"},
            {"id": "SBH", "name": "Saint Barthelemy (SBH)"},
            {"id": "CLY", "name": "Saint Catherine (CLY)"},
            {"id": "STC", "name": "Saint Cloud Regional Airport (STC)"},
            {"id": "LYS", "name": "Saint Exupery (LYS)"},
            {"id": "GNB", "name": "Saint Geoirs (GNB)"},
            {"id": "YSJ", "name": "Saint John (YSJ)"},
            {"id": "XLS", "name": "Saint Louis (XLS)"},
            {"id": "LDX", "name": "Saint-Laurent-du-Maroni Airport (LDX)"},
            {"id": "XSH", "name": "Saint-Pierre-des-Corps (XSH)"},
            {"id": "SMS", "name": "Sainte Marie (SMS)"},
            {"id": "FEZ", "name": "Saiss (FEZ)"},
            {"id": "SNO", "name": "Sakon Nakhon (SNO)"},
            {"id": "MVR", "name": "Salak (MVR)"},
            {"id": "SLL", "name": "Salalah (SLL)"},
            {"id": "SLM", "name": "Salamanca (SLM)"},
            {"id": "RBA", "name": "Sale (RBA)"},
            {"id": "SLY", "name": "Salekhard Airport (SLY)"},
            {"id": "QSR", "name": "Salerno Pontecagnano Airport (QSR)"},
            {"id": "POA", "name": "Salgado Filho (POA)"},
            {"id": "SCX", "name": "Salina Cruz Naval Air Station (SCX)"},
            {"id": "SLN", "name": "Salina Municipal Airport (SLN)"},
            {"id": "SBY", "name": "Salisbury Ocean City Wicomico Rgnl (SBY)"},
            {"id": "YZG", "name": "Salluit Airport (YZG)"},
            {"id": "SLX", "name": "Salt Cay Airport (SLX)"},
            {"id": "SLC", "name": "Salt Lake City Intl (SLC)"},
            {"id": "SLA", "name": "Salta (SLA)"},
            {"id": "6J4", "name": "Saluda County (6J4)"},
            {"id": "CCF", "name": "Salvaza (CCF)"},
            {"id": "SZG", "name": "Salzburg (SZG)"},
            {"id": "MDC", "name": "Sam Ratulangi (MDC)"},
            {"id": "AZS", "name": "Samaná El Catey International Airport (AZS)"},
            {"id": "SKD", "name": "Samarkand (SKD)"},
            {"id": "SVB", "name": "Sambava (SVB)"},
            {"id": "SAX", "name": "Sambu Airport (SAX)"},
            {"id": "UAS", "name": "Samburu South Airport (UAS)"},
            {"id": "SMV", "name": "Samedan (SMV)"},
            {"id": "YJS", "name": "Samjiyon Airport (YJS)"},
            {"id": "SMI", "name": "Samos (SMI)"},
            {"id": "SMQ", "name": "Sampit(Hasan) Airport (SMQ)"},
            {"id": "SSX", "name": "Samsun  (SSX)"},
            {"id": "SZF", "name": "Samsun-Çarşamba Airport (SZF)"},
            {"id": "USM", "name": "Samui (USM)"},
            {"id": "SAQ", "name": "San Andros (SAQ)"},
            {"id": "SJT", "name": "San Angelo Rgnl Mathis Fld (SJT)"},
            {"id": "SVZ", "name": "San Antonio Del Tachira (SVZ)"},
            {"id": "SAT", "name": "San Antonio Intl (SAT)"},
            {"id": "SBD", "name": "San Bernardino International Airport (SBD)"},
            {"id": "SQL", "name": "San Carlos Airport (SQL)"},
            {"id": "BRC", "name": "San Carlos De Bariloche (BRC)"},
            {"id": "SCY", "name": "San Cristóbal Airport (SCY)"},
            {"id": "SAN", "name": "San Diego Intl (SAN)"},
            {"id": "TQR", "name": "San Domino Island Heliport (TQR)"},
            {"id": "SFE", "name": "San Fernando Airport (SFE)"},
            {"id": "SFD", "name": "San Fernando De Apure (SFD)"},
            {"id": "SFO", "name": "San Francisco Intl (SFO)"},
            {"id": "UAQ", "name": "San Juan (UAQ)"},
            {"id": "WSJ", "name": "San Juan - Uganik Seaplane Base (WSJ)"},
            {"id": "ULA", "name": "San Julian (ULA)"},
            {"id": "IPI", "name": "San Luis (IPI)"},
            {"id": "LUQ", "name": "San Luis (LUQ)"},
            {"id": "SBP", "name": "San Luis County Regional Airport (SBP)"},
            {"id": "ALS", "name": "San Luis Valley Regional Airport (ALS)"},
            {"id": "SPR", "name": "San Pedro (SPR)"},
            {"id": "SPY", "name": "San Pedro (SPY)"},
            {"id": "AFA", "name": "San Rafael (AFA)"},
            {"id": "ZSA", "name": "San Salvador (ZSA)"},
            {"id": "EAS", "name": "San Sebastian (EAS)"},
            {"id": "SOM", "name": "San Tome (SOM)"},
            {"id": "SAH", "name": "Sanaa Intl (SAH)"},
            {"id": "SDG", "name": "Sanandaj (SDG)"},
            {"id": "SDP", "name": "Sand Point Airport (SDP)"},
            {"id": "SDK", "name": "Sandakan (SDK)"},
            {"id": "NDY", "name": "Sanday Airport (NDY)"},
            {"id": "YZP", "name": "Sandspit (YZP)"},
            {"id": "ZSJ", "name": "Sandy Lake Airport (ZSJ)"},
            {"id": "SFM", "name": "Sanford Regional (SFM)"},
            {"id": "SGS", "name": "Sanga Sanga Airport (SGS)"},
            {"id": "EAE", "name": "Sangafa Airport (EAE)"},
            {"id": "MBJ", "name": "Sangster Intl (MBJ)"},
            {"id": "TTU", "name": "Saniat Rmel (TTU)"},
            {"id": "YSK", "name": "Sanikiluaq Airport (YSK)"},
            {"id": "SFQ", "name": "Sanliurfa Airport (SFQ)"},
            {"id": "GNY", "name": "Sanliurfa GAP (GNY)"},
            {"id": "TVS", "name": "Sannvhe (TVS)"},
            {"id": "CRC", "name": "Santa Ana Airport (CRC)"},
            {"id": "NNB", "name": "Santa Ana Airport (NNB)"},
            {"id": "STB", "name": "Santa Barbara Del Zulia (STB)"},
            {"id": "SBA", "name": "Santa Barbara Muni (SBA)"},
            {"id": "RZA", "name": "Santa Cruz (RZA)"},
            {"id": "SRZ", "name": "Santa Cruz (SRZ)"},
            {"id": "STU", "name": "Santa Cruz (STU)"},
            {"id": "AQB", "name": "Santa Cruz des Quiche Airport (AQB)"},
            {"id": "SCZ", "name": "Santa Cruz/Graciosa Bay/Luova Airport (SCZ)"},
            {"id": "SNV", "name": "Santa Elena Airport (SNV)"},
            {"id": "SAF", "name": "Santa Fe Muni (SAF)"},
            {"id": "GYN", "name": "Santa Genoveva (GYN)"},
            {"id": "AJU", "name": "Santa Maria (AJU)"},
            {"id": "SMA", "name": "Santa Maria (SMA)"},
            {"id": "RIA", "name": "Santa Maria Airport (RIA)"},
            {"id": "SMX", "name": "Santa Maria Pub Cpt G Allan Hancock Airport (SMX)"},
            {"id": "SMO", "name": "Santa Monica Municipal Airport (SMO)"},
            {"id": "RSA", "name": "Santa Rosa (RSA)"},
            {"id": "SRA", "name": "Santa Rosa Airport (SRA)"},
            {"id": "SST", "name": "Santa Teresita Airport (SST)"},
            {"id": "SDR", "name": "Santander (SDR)"},
            {"id": "STM", "name": "Santarem (STM)"},
            {"id": "SCQ", "name": "Santiago (SCQ)"},
            {"id": "SDE", "name": "Santiago Del Estero (SDE)"},
            {"id": "AUC", "name": "Santiago Perez (AUC)"},
            {"id": "GEL", "name": "Santo Angelo (GEL)"},
            {"id": "SON", "name": "Santo Pekoa International Airport (SON)"},
            {"id": "JTR", "name": "Santorini (JTR)"},
            {"id": "SDU", "name": "Santos Dumont (SDU)"},
            {"id": "QSC", "name": "Sao Carlos TAM (QSC)"},
            {"id": "SXO", "name": "Sao Felix do Araguaia Airport (SXO)"},
            {"id": "SXX", "name": "Sao Felix do Xingu Airport (SXX)"},
            {"id": "SFL", "name": "Sao Filipe Airport (SFL)"},
            {"id": "SJL", "name": "Sao Gabriel da Cachoeira Airport (SJL)"},
            {"id": "SJZ", "name": "Sao Jorge (SJZ)"},
            {"id": "SJP", "name": "Sao Jose Do Rio Preto (SJP)"},
            {"id": "VXE", "name": "Sao Pedro (VXE)"},
            {"id": "TMS", "name": "Sao Tome Intl (TMS)"},
            {"id": "SSR", "name": "Sara Airport (SSR)"},
            {"id": "SJJ", "name": "Sarajevo (SJJ)"},
            {"id": "SKX", "name": "Saransk Airport (SKX)"},
            {"id": "SRQ", "name": "Sarasota Bradenton Intl (SRQ)"},
            {"id": "5B2", "name": "Saratoga County Airport (5B2)"},
            {"id": "VNA", "name": "Saravane Airport (VNA)"},
            {"id": "SRH", "name": "Sarh Airport (SRH)"},
            {"id": "SRY", "name": "Sari Dasht E Naz Airport (SRY)"},
            {"id": "SJX", "name": "Sartaneja Airport (SJX)"},
            {"id": "KGF", "name": "Sary-Arka (KGF)"},
            {"id": "YXE", "name": "Saskatoon J G Diefenbaker Intl (YXE)"},
            {"id": "RTG", "name": "Satar Tacik (RTG)"},
            {"id": "TNI", "name": "Satna (TNI)"},
            {"id": "SUJ", "name": "Satu Mare (SUJ)"},
            {"id": "SFN", "name": "Sauce Viejo (SFN)"},
            {"id": "SAK", "name": "Saudarkrokur (SAK)"},
            {"id": "XAU", "name": "Saul Airport (XAU)"},
            {"id": "YAM", "name": "Sault Ste Marie (YAM)"},
            {"id": "VHC", "name": "Saurimo (VHC)"},
            {"id": "SAV", "name": "Savannah Hilton Head Intl (SAV)"},
            {"id": "ZVK", "name": "Savannakhet (ZVK)"},
            {"id": "GSE", "name": "Save (GSE)"},
            {"id": "SVL", "name": "Savonlinna (SVL)"},
            {"id": "SVA", "name": "Savoonga Airport (SVA)"},
            {"id": "SVU", "name": "Savusavu Airport (SVU)"},
            {"id": "MQT", "name": "Sawyer International Airport (MQT)"},
            {"id": "GXF", "name": "Sayun International Airport (GXF)"},
            {"id": "LKS", "name": "Sazena (LKS)"},
            {"id": "SCM", "name": "Scammon Bay Airport (SCM)"},
            {"id": "SPB", "name": "Scappoose Industrial Airpark (SPB)"},
            {"id": "SDZ", "name": "Scatsta (SDZ)"},
            {"id": "SCS", "name": "Scatsta Airport (SCS)"},
            {"id": "06C", "name": "Schaumburg Regional (06C)"},
            {"id": "YKL", "name": "Schefferville (YKL)"},
            {"id": "AMS", "name": "Schiphol (AMS)"},
            {"id": "GLS", "name": "Scholes Intl At Galveston (GLS)"},
            {"id": "SXF", "name": "Schonefeld (SXF)"},
            {"id": "VIE", "name": "Schwechat (VIE)"},
            {"id": "SZW", "name": "Schwerin Parchim (SZW)"},
            {"id": "NSO", "name": "Scone Airport (NSO)"},
            {"id": "BLV", "name": "Scott Afb Midamerica (BLV)"},
            {"id": "ZSY", "name": "Scottsdale Airport (ZSY)"},
            {"id": "SDV", "name": "Sde Dov (SDV)"},
            {"id": "SYB", "name": "Seal Bay Seaplane Base (SYB)"},
            {"id": "ZSW", "name": "Seal Cove Seaplane Base (ZSW)"},
            {"id": "F57", "name": "Seaplane Base (F57)"},
            {"id": "YPI", "name": "Seaplane Base (YPI)"},
            {"id": "SEA", "name": "Seattle Tacoma Intl (SEA)"},
            {"id": "X26", "name": "Sebastian Municipal (X26)"},
            {"id": "SEB", "name": "Sebha (SEB)"},
            {"id": "YHS", "name": "Sechelt Aerodrome (YHS)"},
            {"id": "SDX", "name": "Sedona (SDX)"},
            {"id": "MCT", "name": "Seeb Intl (MCT)"},
            {"id": "EGM", "name": "Sege Airport (EGM)"},
            {"id": "SYW", "name": "Sehwan Sharif Airport (SYW)"},
            {"id": "SJY", "name": "Seinäjoki Airport (SJY)"},
            {"id": "AMI", "name": "Selaparang (AMI)"},
            {"id": "WLK", "name": "Selawik Airport (WLK)"},
            {"id": "PKW", "name": "Selebi Phikwe (PKW)"},
            {"id": "XSP", "name": "Seletar (XSP)"},
            {"id": "SES", "name": "Selfield Airport (SES)"},
            {"id": "MTC", "name": "Selfridge Angb (MTC)"},
            {"id": "SEY", "name": "Selibady (SEY)"},
            {"id": "PLX", "name": "Semipalatinsk (PLX)"},
            {"id": "PNZ", "name": "Senador Nilo Coelho (PNZ)"},
            {"id": "THE", "name": "Senador Petronio Portella (THE)"},
            {"id": "OLC", "name": "Senadora Eunice Micheles Airport (OLC)"},
            {"id": "SDJ", "name": "Sendai (SDJ)"},
            {"id": "SEH", "name": "Senggeh Airport (SEH)"},
            {"id": "BKO", "name": "Senou (BKO)"},
            {"id": "DJJ", "name": "Sentani (DJJ)"},
            {"id": "XKL", "name": "Sentral (XKL)"},
            {"id": "LEU", "name": "Seo De Urgel (LEU)"},
            {"id": "SSN", "name": "Seoul Ab (SSN)"},
            {"id": "BPN", "name": "Sepinggan (BPN)"},
            {"id": "YZV", "name": "Sept Iles (YZV)"},
            {"id": "SEU", "name": "Seronera (SEU)"},
            {"id": "SVQ", "name": "Sevilla (SVQ)"},
            {"id": "SWD", "name": "Seward Airport (SWD)"},
            {"id": "SEZ", "name": "Seychelles Intl (SEZ)"},
            {"id": "GPS", "name": "Seymour (GPS)"},
            {"id": "GSB", "name": "Seymour Johnson Afb (GSB)"},
            {"id": "SHX", "name": "Shageluk Airport (SHX)"},
            {"id": "CGP", "name": "Shah Amanat Intl (CGP)"},
            {"id": "RJH", "name": "Shah Mokhdum (RJH)"},
            {"id": "KSH", "name": "Shahid Ashrafi Esfahani (KSH)"},
            {"id": "QMJ", "name": "Shahid Asyaee (QMJ)"},
            {"id": "CQD", "name": "Shahre Kord Airport (CQD)"},
            {"id": "SWX", "name": "Shakawe Airport (SWX)"},
            {"id": "SKK", "name": "Shaktoolik Airport (SKK)"},
            {"id": "ZTM", "name": "Shamattawa Airport (ZTM)"},
            {"id": "KNH", "name": "Shang Yi (KNH)"},
            {"id": "SHP", "name": "Shanhaiguan Airport (SHP)"},
            {"id": "SNN", "name": "Shannon (SNN)"},
            {"id": "SHJ", "name": "Sharjah Intl (SHJ)"},
            {"id": "MJK", "name": "Shark Bay Airport (MJK)"},
            {"id": "SSH", "name": "Sharm El Sheikh Intl (SSH)"},
            {"id": "AZ3", "name": "Sharona (AZ3)"},
            {"id": "GSQ", "name": "Sharq Al-Owainat Airport (GSQ)"},
            {"id": "SHW", "name": "Sharurah (SHW)"},
            {"id": "SSC", "name": "Shaw Afb (SSC)"},
            {"id": "YAW", "name": "Shearwater (YAW)"},
            {"id": "SBM", "name": "Sheboygan County Memorial Airport (SBM)"},
            {"id": "RYK", "name": "Sheikh Zayed (RYK)"},
            {"id": "2H0", "name": "Shelby County Airport (2H0)"},
            {"id": "EET", "name": "Shelby County Airport (EET)"},
            {"id": "SXP", "name": "Sheldon Point Airport (SXP)"},
            {"id": "PTZ", "name": "Shell Mera (PTZ)"},
            {"id": "SHD", "name": "Shenandoah Valley Regional Airport (SHD)"},
            {"id": "SHE", "name": "Shenyang Taoxian International Airport (SHE)"},
            {"id": "SPS", "name": "Sheppard Afb Wichita Falls Muni (SPS)"},
            {"id": "SHT", "name": "Shepparton (SHT)"},
            {"id": "BTE", "name": "Sherbro International Airport (BTE)"},
            {"id": "YSC", "name": "Sherbrooke (YSC)"},
            {"id": "SVO", "name": "Sheremetyevo (SVO)"},
            {"id": "SHR", "name": "Sheridan County Airport (SHR)"},
            {"id": "FLV", "name": "Sherman Aaf (FLV)"},
            {"id": "RKZ", "name": "Shigatse Peace Airport (RKZ)"},
            {"id": "SJW", "name": "Shijiazhuang Daguocun International Airport (SJW)"},
            {"id": "HIL", "name": "Shilavo Airport (HIL)"},
            {"id": "SHL", "name": "Shillong Airport (SHL)"},
            {"id": "SLV", "name": "Shimla Airport (SLV)"},
            {"id": "SHI", "name": "Shimojishima (SHI)"},
            {"id": "SHY", "name": "Shinyanga Airport (SHY)"},
            {"id": "SYZ", "name": "Shiraz Shahid Dastghaib Intl (SYZ)"},
            {"id": "SHC", "name": "Shire Inda Selassie Airport (SHC)"},
            {"id": "SHH", "name": "Shishmaref Airport (SHH)"},
            {"id": "SAA", "name": "Shively Field Airport (SAA)"},
            {"id": "0P2", "name": "Shoestring Aviation Airfield (0P2)"},
            {"id": "SSE", "name": "Sholapur (SSE)"},
            {"id": "SYO", "name": "Shonai Airport (SYO)"},
            {"id": "ESH", "name": "Shoreham (ESH)"},
            {"id": "SOW", "name": "Show Low Regional Airport (SOW)"},
            {"id": "STW", "name": "Shpakovskoye (STW)"},
            {"id": "SHV", "name": "Shreveport Rgnl (SHV)"},
            {"id": "CTU", "name": "Shuangliu (CTU)"},
            {"id": "LYI", "name": "Shubuling Airport (LYI)"},
            {"id": "SHG", "name": "Shungnak Airport (SHG)"},
            {"id": "JHQ", "name": "Shute Harbour (JHQ)"},
            {"id": "CIT", "name": "Shymkent (CIT)"},
            {"id": "SKT", "name": "Sialkot Airport (SKT)"},
            {"id": "SQQ", "name": "Siauliai Intl (SQQ)"},
            {"id": "SBZ", "name": "Sibiu (SBZ)"},
            {"id": "SBW", "name": "Sibu (SBW)"},
            {"id": "TGR", "name": "Sidi Mahdi (TGR)"},
            {"id": "SNY", "name": "Sidney Muni Airport (SNY)"},
            {"id": "SDY", "name": "Sidney-Richland Municipal Airport (SDY)"},
            {"id": "REP", "name": "Siem Reap (REP)"},
            {"id": "SRR", "name": "Sierra Blanca Regional Airport (SRR)"},
            {"id": "SGV", "name": "Sierra Grande (SGV)"},
            {"id": "MZO", "name": "Sierra Maestra (MZO)"},
            {"id": "FHU", "name": "Sierra Vista Muni Libby Aaf (FHU)"},
            {"id": "GIU", "name": "Sigiriya Airport (GIU)"},
            {"id": "SIJ", "name": "Siglufjordur (SIJ)"},
            {"id": "NSY", "name": "Sigonella (NSY)"},
            {"id": "KOS", "name": "Sihanoukville (KOS)"},
            {"id": "SIK", "name": "Sikeston Memorial Municipal (SIK)"},
            {"id": "IXS", "name": "Silchar (IXS)"},
            {"id": "SPZ", "name": "Silver Springs Airport (SPZ)"},
            {"id": "ASU", "name": "Silvio Pettirossi Intl (ASU)"},
            {"id": "SYM", "name": "Simao Airport (SYM)"},
            {"id": "SIF", "name": "Simara (SIF)"},
            {"id": "SIM", "name": "Simbai (SIM)"},
            {"id": "SIP", "name": "Simferopol Intl (SIP)"},
            {"id": "IMK", "name": "Simikot (IMK)"},
            {"id": "SMR", "name": "Simon Bolivar (SMR)"},
            {"id": "CCS", "name": "Simon Bolivar Intl (CCS)"},
            {"id": "CNL", "name": "Sindal Airport (CNL)"},
            {"id": "INY", "name": "Singita Sabi Sands (INY)"},
            {"id": "OPS", "name": "Sinop Airport (OPS)"},
            {"id": "SIC", "name": "Sinop Airport (SIC)"},
            {"id": "SIR", "name": "Sion (SIR)"},
            {"id": "FSD", "name": "Sioux Falls (FSD)"},
            {"id": "SUX", "name": "Sioux Gateway Col Bud Day Fld (SUX)"},
            {"id": "YXL", "name": "Sioux Lookout (YXL)"},
            {"id": "XSB", "name": "Sir Bani Yas Island (XSB)"},
            {"id": "MRU", "name": "Sir Seewoosagur Ramgoolam Intl (MRU)"},
            {"id": "GBE", "name": "Sir Seretse Khama Intl (GBE)"},
            {"id": "SIS", "name": "Sishen (SIS)"},
            {"id": "JHS", "name": "Sisimiut Airport (JHS)"},
            {"id": "JSH", "name": "Sitia (JSH)"},
            {"id": "SIT", "name": "Sitka Rocky Gutierrez (SIT)"},
            {"id": "AKY", "name": "Sittwe (AKY)"},
            {"id": "SIU", "name": "Siuna Airport (SIU)"},
            {"id": "VAS", "name": "Sivas (VAS)"},
            {"id": "SKN", "name": "Skagen (SKN)"},
            {"id": "SGY", "name": "Skagway Airport (SGY)"},
            {"id": "KDU", "name": "Skardu Airport (KDU)"},
            {"id": "NYO", "name": "Skavsta (NYO)"},
            {"id": "SFT", "name": "Skelleftea (SFT)"},
            {"id": "SKU", "name": "Skiros (SKU)"},
            {"id": "SKP", "name": "Skopje (SKP)"},
            {"id": "KVB", "name": "Skovde (KVB)"},
            {"id": "SKS", "name": "Skrydstrup (SKS)"},
            {"id": "SZK", "name": "Skukuza (SZK)"},
            {"id": "DAW", "name": "Skyhaven Airport (DAW)"},
            {"id": "YZH", "name": "Slave Lake (YZH)"},
            {"id": "SLQ", "name": "Sleetmute Airport (SLQ)"},
            {"id": "SLD", "name": "Sliac (SLD)"},
            {"id": "SXL", "name": "Sligo (SXL)"},
            {"id": "ISN", "name": "Sloulin Fld Intl (ISN)"},
            {"id": "SMW", "name": "Smara Airport (SMW)"},
            {"id": "SMD", "name": "Smith Fld (SMD)"},
            {"id": "INT", "name": "Smith Reynolds (INT)"},
            {"id": "YYD", "name": "Smithers (YYD)"},
            {"id": "PAE", "name": "Snohomish Co (PAE)"},
            {"id": "DWB", "name": "Soalala Airport (DWB)"},
            {"id": "AER", "name": "Sochi (AER)"},
            {"id": "SCT", "name": "Socotra Intl (SCT)"},
            {"id": "SOT", "name": "Sodankyla (SOT)"},
            {"id": "SOO", "name": "Soderhamn Airport (SOO)"},
            {"id": "CGK", "name": "Soekarno Hatta Intl (CGK)"},
            {"id": "UTC", "name": "Soesterberg (UTC)"},
            {"id": "SOF", "name": "Sofia (SOF)"},
            {"id": "SOG", "name": "Sogndal Airport (SOG)"},
            {"id": "HMB", "name": "Sohag International (HMB)"},
            {"id": "SHO", "name": "Sokcho (SHO)"},
            {"id": "KMW", "name": "Sokerkino (KMW)"},
            {"id": "GDX", "name": "Sokol (GDX)"},
            {"id": "SVG", "name": "Sola (SVG)"},
            {"id": "SLH", "name": "Sola Airport (SLH)"},
            {"id": "SXQ", "name": "Soldotna Airport (SXQ)"},
            {"id": "SOZ", "name": "Solenzara (SOZ)"},
            {"id": "SLJ", "name": "Solomon Airport (SLJ)"},
            {"id": "CSH", "name": "Solovki Airport (CSH)"},
            {"id": "SLI", "name": "Solwesi Airport (SLI)"},
            {"id": "2G9", "name": "Somerset County Airport (2G9)"},
            {"id": "PMI", "name": "Son Sant Joan (PMI)"},
            {"id": "SGD", "name": "Sonderborg (SGD)"},
            {"id": "NOE", "name": "Sonderlandeplatz Norden-Norddeich (NOE)"},
            {"id": "DSO", "name": "Sondok Airport (DSO)"},
            {"id": "SFJ", "name": "Sondre Stromfjord (SFJ)"},
            {"id": "SOJ", "name": "Sorkjosen Airport (SOJ)"},
            {"id": "SOD", "name": "Sorocaba Airport (SOD)"},
            {"id": "SRT", "name": "Soroti (SRT)"},
            {"id": "SRP", "name": "Sorstokken (SRP)"},
            {"id": "NIT", "name": "Souche (NIT)"},
            {"id": "CHQ", "name": "Souda (CHQ)"},
            {"id": "BJA", "name": "Soummam (BJA)"},
            {"id": "SFK", "name": "Soure Airport (SFK)"},
            {"id": "ELD", "name": "South Arkansas Rgnl At Goodwin Fld (ELD)"},
            {"id": "SBN", "name": "South Bend Rgnl (SBN)"},
            {"id": "BIM", "name": "South Bimini (BIM)"},
            {"id": "XSC", "name": "South Caicos (XSC)"},
            {"id": "ZML", "name": "South Cariboo Regional Airport (ZML)"},
            {"id": "LWA", "name": "South Haven Area Regional Airport (LWA)"},
            {"id": "XSI", "name": "South Indian Lake Airport (XSI)"},
            {"id": "VAY", "name": "South Jersey Regional Airport (VAY)"},
            {"id": "X49", "name": "South Lakeland Airport (X49)"},
            {"id": "WSN", "name": "South Naknek Airport (WSN)"},
            {"id": "SOU", "name": "Southampton (SOU)"},
            {"id": "KIW", "name": "Southdowns (KIW)"},
            {"id": "BRL", "name": "Southeast Iowa Regional Airport (BRL)"},
            {"id": "BPT", "name": "Southeast Texas Rgnl (BPT)"},
            {"id": "SEN", "name": "Southend (SEN)"},
            {"id": "VCV", "name": "Southern California Logistics (VCV)"},
            {"id": "JVL", "name": "Southern Wisconsin Regional Airport (JVL)"},
            {"id": "YPG", "name": "Southport (YPG)"},
            {"id": "SWJ", "name": "Southwest Bay Airport (SWJ)"},
            {"id": "RSW", "name": "Southwest Florida Intl (RSW)"},
            {"id": "ABY", "name": "Southwest Georgia Regional Airport (ABY)"},
            {"id": "BEH", "name": "Southwest Michigan Regional Airport (BEH)"},
            {"id": "OTH", "name": "Southwest Oregon Regional Airport (OTH)"},
            {"id": "OVS", "name": "Sovetsky Tyumenskaya Airport (OVS)"},
            {"id": "SZA", "name": "Soyo (SZA)"},
            {"id": "TIX", "name": "Space Coast Reg'l Airport (TIX)"},
            {"id": "SPM", "name": "Spangdahlem Ab (SPM)"},
            {"id": "SVW", "name": "Sparrevohn Lrrs (SVW)"},
            {"id": "SPW", "name": "Spencer Muni (SPW)"},
            {"id": "ZQC", "name": "Speyer (ZQC)"},
            {"id": "NOZ", "name": "Spichenkovo Airport (NOZ)"},
            {"id": "SUS", "name": "Spirit Of St Louis (SUS)"},
            {"id": "7N7", "name": "Spitfire Aerodrome (7N7)"},
            {"id": "SPU", "name": "Split (SPU)"},
            {"id": "GEG", "name": "Spokane Intl (GEG)"},
            {"id": "70N", "name": "Spring Hill Airport (70N)"},
            {"id": "AXP", "name": "Spring Point (AXP)"},
            {"id": "SBU", "name": "Springbok (SBU)"},
            {"id": "ZSF", "name": "Springfield Amtrak Station (ZSF)"},
            {"id": "SGF", "name": "Springfield Branson Natl (SGF)"},
            {"id": "SGH", "name": "Springfield-Beckly Municipal Airport (SGH)"},
            {"id": "SXR", "name": "Srinagar (SXR)"},
            {"id": "RCO", "name": "St Agnant (RCO)"},
            {"id": "ENK", "name": "St Angelo (ENK)"},
            {"id": "YAY", "name": "St Anthony (YAY)"},
            {"id": "YIF", "name": "St Augustin Airport (YIF)"},
            {"id": "SKV", "name": "St Catherine Intl (SKV)"},
            {"id": "PHN", "name": "St Clair Co Intl (PHN)"},
            {"id": "RUN", "name": "St Denis Gillot (RUN)"},
            {"id": "ACH", "name": "St Gallen Altenrhein (ACH)"},
            {"id": "DOL", "name": "St Gatien (DOL)"},
            {"id": "PBV", "name": "St George (PBV)"},
            {"id": "SGO", "name": "St George Airport (SGO)"},
            {"id": "SGU", "name": "St George Muni (SGU)"},
            {"id": "YHU", "name": "St Hubert (YHU)"},
            {"id": "RNS", "name": "St Jacques (RNS)"},
            {"id": "YJN", "name": "St Jean (YJN)"},
            {"id": "YYT", "name": "St Johns Intl (YYT)"},
            {"id": "FRP", "name": "St Lucie County International Airport (FRP)"},
            {"id": "KSM", "name": "St Marys Airport (KSM)"},
            {"id": "NQY", "name": "St Mawgan (NQY)"},
            {"id": "QQS", "name": "St Pancras Railway Station (QQS)"},
            {"id": "SNP", "name": "St Paul Island (SNP)"},
            {"id": "PIE", "name": "St Petersburg Clearwater Intl (PIE)"},
            {"id": "FSP", "name": "St Pierre (FSP)"},
            {"id": "ZSE", "name": "St Pierre Pierrefonds (ZSE)"},
            {"id": "SFC", "name": "St-François Airport (SFC)"},
            {"id": "SGJ", "name": "St. Augustine Airport (SGJ)"},
            {"id": "UST", "name": "St. Augustine Airport (UST)"},
            {"id": "STG", "name": "St. George Airport (STG)"},
            {"id": "YFX", "name": "St. Lewis (Fox Harbour) Airport (YFX)"},
            {"id": "CPS", "name": "St. Louis Downtown Airport (CPS)"},
            {"id": "SMK", "name": "St. Michael Airport (SMK)"},
            {"id": "PSH", "name": "St. Peter-Ording Airport (PSH)"},
            {"id": "YST", "name": "St. Theresa Point Airport (YST)"},
            {"id": "HMR", "name": "Stafsberg Airport (HMR)"},
            {"id": "KBU", "name": "Stagen Airport (KBU)"},
            {"id": "ZTF", "name": "Stamford Amtrak Station (ZTF)"},
            {"id": "ND4", "name": "Stanhope (ND4)"},
            {"id": "PSY", "name": "Stanley Airport (PSY)"},
            {"id": "STN", "name": "Stansted (STN)"},
            {"id": "RYB", "name": "Staroselye Airport (RYB)"},
            {"id": "STA", "name": "Stauning (STA)"},
            {"id": "SBS", "name": "Steamboat Springs Airport-Bob Adams Field (SBS)"},
            {"id": "WBB", "name": "Stebbins Airport (WBB)"},
            {"id": "SCV", "name": "Stefan Cel Mare (SCV)"},
            {"id": "SML", "name": "Stella Maris (SML)"},
            {"id": "ZSN", "name": "Stendal Borstel (ZSN)"},
            {"id": "BKD", "name": "Stephens Co (BKD)"},
            {"id": "YJT", "name": "Stephenville (YJT)"},
            {"id": "STK", "name": "Sterling Municipal Airport (STK)"},
            {"id": "XVJ", "name": "Stevenage Railway Station (XVJ)"},
            {"id": "STE", "name": "Stevens Point Municipal Airport (STE)"},
            {"id": "SWF", "name": "Stewart Intl (SWF)"},
            {"id": "STO", "name": "Stockholm Cruise Port (STO)"},
            {"id": "SCK", "name": "Stockton Metropolitan (SCK)"},
            {"id": "SMZ", "name": "Stoelmans Eiland Airstrip (SMZ)"},
            {"id": "SSJ", "name": "Stokka (SSJ)"},
            {"id": "YSF", "name": "Stony Rapids Airport (YSF)"},
            {"id": "SRV", "name": "Stony River 2 Airport (SRV)"},
            {"id": "N69", "name": "Stormville Airport (N69)"},
            {"id": "SYY", "name": "Stornoway (SYY)"},
            {"id": "SQO", "name": "Storuman Airport (SQO)"},
            {"id": "WRO", "name": "Strachowice (WRO)"},
            {"id": "SRN", "name": "Strahan Airport (SRN)"},
            {"id": "SR2", "name": "Stranraer Ferry Port (SR2)"},
            {"id": "SCH", "name": "Stratton ANGB - Schenectady County Airpor (SCH)"},
            {"id": "SWT", "name": "Strezhevoy (SWT)"},
            {"id": "SOY", "name": "Stronsay Airport (SOY)"},
            {"id": "N53", "name": "Stroudsburg-Pocono Airport (N53)"},
            {"id": "MMX", "name": "Sturup (MMX)"},
            {"id": "STR", "name": "Stuttgart (STR)"},
            {"id": "ZWS", "name": "Stuttgart Railway Station (ZWS)"},
            {"id": "VAO", "name": "Suavanao Airport (VAO)"},
            {"id": "SFH", "name": "Sub Teniente Nestor Arias (SFH)"},
            {"id": "SZB", "name": "Subang-Sultan Abdul Aziz Shah Intl (SZB)"},
            {"id": "SFS", "name": "Subic Bay International Airport (SFS)"},
            {"id": "FSC", "name": "Sud Corse (FSC)"},
            {"id": "YSB", "name": "Sudbury (YSB)"},
            {"id": "YSD", "name": "Suffield Heliport (YSD)"},
            {"id": "SGR", "name": "Sugar Land Regional Airport (SGR)"},
            {"id": "AFS", "name": "Sugraly Airport (AFS)"},
            {"id": "SUL", "name": "Sui (SUL)"},
            {"id": "THS", "name": "Sukhothai (THS)"},
            {"id": "SUI", "name": "Sukhumi Dranda (SUI)"},
            {"id": "SKC", "name": "Suki Airport (SKC)"},
            {"id": "SKZ", "name": "Sukkur (SKZ)"},
            {"id": "SLF", "name": "Sulayel (SLF)"},
            {"id": "ISU", "name": "Sulaymaniyah International Airport (ISU)"},
            {"id": "AOR", "name": "Sultan Abdul Halim (AOR)"},
            {"id": "IPH", "name": "Sultan Azlan Shah (IPH)"},
            {"id": "TTE", "name": "Sultan Babullah (TTE)"},
            {"id": "BTJ", "name": "Sultan Iskandarmuda (BTJ)"},
            {"id": "JHB", "name": "Sultan Ismail (JHB)"},
            {"id": "KBR", "name": "Sultan Ismail Petra (KBR)"},
            {"id": "TGG", "name": "Sultan Mahmud (TGG)"},
            {"id": "PLM", "name": "Sultan Mahmud Badaruddin Ii (PLM)"},
            {"id": "PKU", "name": "Sultan Syarif Kasim Ii (PKU)"},
            {"id": "DJB", "name": "Sultan Thaha (DJB)"},
            {"id": "SWQ", "name": "Sumbawa Besar Airport (SWQ)"},
            {"id": "LSI", "name": "Sumburgh (LSI)"},
            {"id": "SUR", "name": "Summer Beaver Airport (SUR)"},
            {"id": "YSU", "name": "Summerside (YSU)"},
            {"id": "MSI", "name": "Sun Island Airport (MSI)"},
            {"id": "SMT", "name": "Sun Moon Lake Airport (SMT)"},
            {"id": "SDL", "name": "Sundsvall Harnosand (SDL)"},
            {"id": "TSA", "name": "Sungshan (TSA)"},
            {"id": "MCY", "name": "Sunshine Coast (MCY)"},
            {"id": "NYI", "name": "Sunyani (NYI)"},
            {"id": "PNK", "name": "Supadio (PNK)"},
            {"id": "STV", "name": "Surat (STV)"},
            {"id": "URT", "name": "Surat Thani (URT)"},
            {"id": "SGC", "name": "Surgut (SGC)"},
            {"id": "SUG", "name": "Surigao Airport (SUG)"},
            {"id": "SKH", "name": "Surkhet (SKH)"},
            {"id": "SQG", "name": "Susilo Airport (SQG)"},
            {"id": "GED", "name": "Sussex Co (GED)"},
            {"id": "BKK", "name": "Suvarnabhumi Intl (BKK)"},
            {"id": "24J", "name": "Suwannee County Airport (24J)"},
            {"id": "VAW", "name": "Svartnes Airport (VAW)"},
            {"id": "EVG", "name": "Sveg (EVG)"},
            {"id": "SVJ", "name": "Svolvær Helle Airport (SVJ)"},
            {"id": "SWP", "name": "Swakopmund Airport (SWP)"},
            {"id": "ZJN", "name": "Swan River Airport (ZJN)"},
            {"id": "SWS", "name": "Swansea (SWS)"},
            {"id": "YYN", "name": "Swift Current (YYN)"},
            {"id": "BDJ", "name": "Syamsudin Noor (BDJ)"},
            {"id": "SYH", "name": "Syangboche (SYH)"},
            {"id": "YQY", "name": "Sydney (YQY)"},
            {"id": "BWU", "name": "Sydney Bankstown (BWU)"},
            {"id": "SYD", "name": "Sydney Intl (SYD)"},
            {"id": "SCW", "name": "Syktyvkar (SCW)"},
            {"id": "C89", "name": "Sylvania Airport (C89)"},
            {"id": "SYR", "name": "Syracuse Hancock Intl (SYR)"},
            {"id": "JSY", "name": "Syros Airport (JSY)"},
            {"id": "SOB", "name": "Sármellék International Airport (SOB)"},
            {"id": "TCP", "name": "Taba Intl (TCP)"},
            {"id": "TBT", "name": "Tabatinga (TBT)"},
            {"id": "TBF", "name": "Tabiteuea North (TBF)"},
            {"id": "TSU", "name": "Tabiteuea South Airport (TSU)"},
            {"id": "TBO", "name": "Tabora Airport (TBO)"},
            {"id": "TBZ", "name": "Tabriz Intl (TBZ)"},
            {"id": "TBG", "name": "Tabubil Airport (TBG)"},
            {"id": "TUU", "name": "Tabuk (TUU)"},
            {"id": "TCG", "name": "Tacheng Airport (TCG)"},
            {"id": "THL", "name": "Tachileik (THL)"},
            {"id": "TIW", "name": "Tacoma Narrows Airport (TIW)"},
            {"id": "ATP", "name": "Tadji Airport (ATP)"},
            {"id": "TDJ", "name": "Tadjoura (TDJ)"},
            {"id": "XTL", "name": "Tadoule Lake Airport (XTL)"},
            {"id": "TAF", "name": "Tafaraoui (TAF)"},
            {"id": "TAG", "name": "Tagbilaran (TAG)"},
            {"id": "THZ", "name": "Tahoua (THZ)"},
            {"id": "TXG", "name": "Taichung Airport (TXG)"},
            {"id": "TIF", "name": "Taif (TIF)"},
            {"id": "TNN", "name": "Tainan (TNN)"},
            {"id": "HRB", "name": "Taiping (HRB)"},
            {"id": "TAI", "name": "Taiz Intl (TAI)"},
            {"id": "TJH", "name": "Tajima Airport (TJH)"},
            {"id": "PAZ", "name": "Tajin (PAZ)"},
            {"id": "TKT", "name": "Tak (TKT)"},
            {"id": "KTF", "name": "Takaka Aerodrome (KTF)"},
            {"id": "TAK", "name": "Takamatsu (TAK)"},
            {"id": "TKP", "name": "Takapoto (TKP)"},
            {"id": "TKX", "name": "Takaroa (TKX)"},
            {"id": "TKD", "name": "Takoradi (TKD)"},
            {"id": "TCT", "name": "Takotna Airport (TCT)"},
            {"id": "ARH", "name": "Talagi (ARH)"},
            {"id": "BJZ", "name": "Talavera La Real (BJZ)"},
            {"id": "TDK", "name": "Taldykorgan Airport (TDK)"},
            {"id": "BDN", "name": "Talhar (BDN)"},
            {"id": "TKA", "name": "Talkeetna (TKA)"},
            {"id": "TLH", "name": "Tallahassee Rgnl (TLH)"},
            {"id": "TLL", "name": "Tallinn (TLL)"},
            {"id": "YYH", "name": "Taloyoak (YYH)"},
            {"id": "TML", "name": "Tamale (TML)"},
            {"id": "TMN", "name": "Tamana Airport (TMN)"},
            {"id": "TMR", "name": "Tamanrasset (TMR)"},
            {"id": "TUD", "name": "Tambacounda (TUD)"},
            {"id": "WTA", "name": "Tambohorano Airport (WTA)"},
            {"id": "TMC", "name": "Tambolaka Airport (TMC)"},
            {"id": "TMU", "name": "Tambor Airport (TMU)"},
            {"id": "TBW", "name": "Tambow (TBW)"},
            {"id": "TME", "name": "Tame (TME)"},
            {"id": "VDF", "name": "Tampa Executive Airport (VDF)"},
            {"id": "TPA", "name": "Tampa Intl (TPA)"},
            {"id": "X39", "name": "Tampa North Aero Park (X39)"},
            {"id": "MJU", "name": "Tampa Padang (MJU)"},
            {"id": "TMP", "name": "Tampere Pirkkala (TMP)"},
            {"id": "TSL", "name": "Tamuin (TSL)"},
            {"id": "TMW", "name": "Tamworth (TMW)"},
            {"id": "TTR", "name": "Tana Toraja Airport (TTR)"},
            {"id": "TAL", "name": "Tanana Airport (TAL)"},
            {"id": "CNF", "name": "Tancredo Neves Intl (CNF)"},
            {"id": "GPB", "name": "Tancredo Thomaz de Faria Airport (GPB)"},
            {"id": "TDG", "name": "Tandag Airport (TDG)"},
            {"id": "TDL", "name": "Tandil (TDL)"},
            {"id": "TGT", "name": "Tanga (TGT)"},
            {"id": "TJS", "name": "Tanjung Harapan Airport (TJS)"},
            {"id": "TGC", "name": "Tanjung Manis Airport (TGC)"},
            {"id": "TAH", "name": "Tanna island (TAH)"},
            {"id": "SGN", "name": "Tansonnhat Intl (SGN)"},
            {"id": "TPE", "name": "Taoyuan Intl (TPE)"},
            {"id": "TAP", "name": "Tapachula Intl (TAP)"},
            {"id": "TPI", "name": "Tapini Airport (TPI)"},
            {"id": "TPJ", "name": "Taplejung (TPJ)"},
            {"id": "IRZ", "name": "Tapuruquara Airport (IRZ)"},
            {"id": "TCD", "name": "Tarapacá Airport (TCD)"},
            {"id": "TPC", "name": "Tarapoa (TPC)"},
            {"id": "XVF", "name": "Tarare (XVF)"},
            {"id": "DMB", "name": "Taraz (DMB)"},
            {"id": "TRO", "name": "Taree Airport (TRO)"},
            {"id": "TIZ", "name": "Tari Airport (TIZ)"},
            {"id": "TII", "name": "Tarin Kowt Airport (TII)"},
            {"id": "TAY", "name": "Tartu (TAY)"},
            {"id": "AGM", "name": "Tasiilaq (AGM)"},
            {"id": "YTQ", "name": "Tasiujaq Airport (YTQ)"},
            {"id": "TLJ", "name": "Tatalina Lrrs (TLJ)"},
            {"id": "TEK", "name": "Tatitlek Airport (TEK)"},
            {"id": "TAT", "name": "Tatry (TAT)"},
            {"id": "TAN", "name": "Taunton Municipal Airport - King Field (TAN)"},
            {"id": "TUO", "name": "Taupo (TUO)"},
            {"id": "TRG", "name": "Tauranga (TRG)"},
            {"id": "BAY", "name": "Tautii Magheraus (BAY)"},
            {"id": "DLE", "name": "Tavaux (DLE)"},
            {"id": "PBJ", "name": "Tavie Airport (PBJ)"},
            {"id": "TWU", "name": "Tawau (TWU)"},
            {"id": "RZP", "name": "Taytay Sandoval (RZP)"},
            {"id": "TBS", "name": "Tbilisi (TBS)"},
            {"id": "TCH", "name": "Tchibanga Airport (TCH)"},
            {"id": "ANC", "name": "Ted Stevens Anchorage Intl (ANC)"},
            {"id": "TFF", "name": "Tefe (TFF)"},
            {"id": "TXL", "name": "Tegel (TXL)"},
            {"id": "TCN", "name": "Tehuacan (TCN)"},
            {"id": "TEQ", "name": "Tekirdağ Çorlu Airport (TEQ)"},
            {"id": "TEA", "name": "Tela (TEA)"},
            {"id": "TFM", "name": "Telefomin Airport (TFM)"},
            {"id": "TEF", "name": "Telfer Airport (TEF)"},
            {"id": "TLA", "name": "Teller Airport (TLA)"},
            {"id": "TEX", "name": "Telluride (TEX)"},
            {"id": "SRI", "name": "Temindung (SRI)"},
            {"id": "TEM", "name": "Temora (TEM)"},
            {"id": "THF", "name": "Tempelhof (THF)"},
            {"id": "UDI", "name": "Ten Cel Av Cesar Bombonato (UDI)"},
            {"id": "TKE", "name": "Tenakee Seaplane Base (TKE)"},
            {"id": "TFN", "name": "Tenerife Norte (TFN)"},
            {"id": "TFS", "name": "Tenerife Sur (TFS)"},
            {"id": "TCZ", "name": "Tengchong Tuofeng Airport (TCZ)"},
            {"id": "CUZ", "name": "Teniente Alejandro Velasco Astete Intl (CUZ)"},
            {"id": "TUC", "name": "Teniente Benjamin Matienzo (TUC)"},
            {"id": "TUA", "name": "Teniente Coronel Luis A Mantilla (TUA)"},
            {"id": "CHM", "name": "Teniente Jaime A De Montreuil Morales (CHM)"},
            {"id": "GXQ", "name": "Teniente Vidal (GXQ)"},
            {"id": "TCA", "name": "Tennant Creek Airport (TCA)"},
            {"id": "TPQ", "name": "Tepic (TPQ)"},
            {"id": "RHD", "name": "Termal (RHD)"},
            {"id": "TMJ", "name": "Termez Airport (TMJ)"},
            {"id": "TNL", "name": "Ternopol (TNL)"},
            {"id": "YXT", "name": "Terrace (YXT)"},
            {"id": "TCY", "name": "Terrace Bay (TCY)"},
            {"id": "EIS", "name": "Terrance B Lettsome Intl (EIS)"},
            {"id": "HUF", "name": "Terre Haute Intl Hulman Fld (HUF)"},
            {"id": "LSS", "name": "Terre-de-Haut Airport (LSS)"},
            {"id": "YZW", "name": "Teslin (YZW)"},
            {"id": "TET", "name": "Tete Chingodzi (TET)"},
            {"id": "TEB", "name": "Teterboro (TEB)"},
            {"id": "TTI", "name": "Tetiaroa Airport (TTI)"},
            {"id": "YGB", "name": "Texada Gillies Bay Airport (YGB)"},
            {"id": "TXK", "name": "Texarkana Rgnl Webb Fld (TXK)"},
            {"id": "BEV", "name": "Teyman (BEV)"},
            {"id": "TEZ", "name": "Tezpur Airport (TEZ)"},
            {"id": "TMK", "name": "Thamkharka (TMK)"},
            {"id": "SNW", "name": "Thandwe (SNW)"},
            {"id": "THG", "name": "Thangool (THG)"},
            {"id": "XTG", "name": "Thargomindah Airport (XTG)"},
            {"id": "YQD", "name": "The Pas Airport (YQD)"},
            {"id": "TDR", "name": "Theodore (TDR)"},
            {"id": "PVD", "name": "Theodore Francis Green State (PVD)"},
            {"id": "TVF", "name": "Thief River Falls (TVF)"},
            {"id": "TRV", "name": "Thiruvananthapuram Intl (TRV)"},
            {"id": "TED", "name": "Thisted (TED)"},
            {"id": "THD", "name": "Tho Xuan Airport (THD)"},
            {"id": "TVI", "name": "Thomasville Regional Airport (TVI)"},
            {"id": "YTH", "name": "Thompson (YTH)"},
            {"id": "KTB", "name": "Thorne Bay Seaplane Base (KTB)"},
            {"id": "THO", "name": "Thorshofn Airport (THO)"},
            {"id": "THU", "name": "Thule Air Base (THU)"},
            {"id": "TTH", "name": "Thumrait (TTH)"},
            {"id": "YQT", "name": "Thunder Bay (YQT)"},
            {"id": "SFA", "name": "Thyna (SFA)"},
            {"id": "WUH", "name": "Tianhe (WUH)"},
            {"id": "THQ", "name": "Tianshui Airport (THQ)"},
            {"id": "AEB", "name": "Tianyang (AEB)"},
            {"id": "TIY", "name": "Tidjikja (TIY)"},
            {"id": "TGJ", "name": "Tiga Airport (TGJ)"},
            {"id": "TIH", "name": "Tikehau (TIH)"},
            {"id": "TKC", "name": "Tiko (TKC)"},
            {"id": "IKS", "name": "Tiksi Airport (IKS)"},
            {"id": "BVA", "name": "Tille (BVA)"},
            {"id": "TIU", "name": "Timaru (TIU)"},
            {"id": "TMX", "name": "Timimoun (TMX)"},
            {"id": "YXR", "name": "Timiskaming Rgnl (YXR)"},
            {"id": "YTS", "name": "Timmins (YTS)"},
            {"id": "TNC", "name": "Tin City LRRS Airport (TNC)"},
            {"id": "TIC", "name": "Tinak Airport (TIC)"},
            {"id": "KTR", "name": "Tindal Airport (KTR)"},
            {"id": "TIN", "name": "Tindouf (TIN)"},
            {"id": "TIQ", "name": "Tinian Intl (TIQ)"},
            {"id": "TIK", "name": "Tinker Afb (TIK)"},
            {"id": "KTP", "name": "Tinson Pen (KTP)"},
            {"id": "TIE", "name": "Tippi Airport (TIE)"},
            {"id": "FME", "name": "Tipton (FME)"},
            {"id": "TPN", "name": "Tiputini (TPN)"},
            {"id": "TIA", "name": "Tirana Rinas (TIA)"},
            {"id": "TRE", "name": "Tiree (TRE)"},
            {"id": "TIR", "name": "Tirupati (TIR)"},
            {"id": "DJG", "name": "Tiska (DJG)"},
            {"id": "TIV", "name": "Tivat (TIV)"},
            {"id": "PKY", "name": "Tjilik Riwut (PKY)"},
            {"id": "TXA", "name": "Tlaxcala (TXA)"},
            {"id": "TMM", "name": "Toamasina (TMM)"},
            {"id": "SYQ", "name": "Tobias Bolanos International Airport (SYQ)"},
            {"id": "TOC", "name": "Toccoa RG Letourneau Field Airport (TOC)"},
            {"id": "PTY", "name": "Tocumen Intl (PTY)"},
            {"id": "YAZ", "name": "Tofino (YAZ)"},
            {"id": "TOG", "name": "Togiak Airport (TOG)"},
            {"id": "6K8", "name": "Tok Junction Airport (6K8)"},
            {"id": "OOK", "name": "Toksook Bay Airport (OOK)"},
            {"id": "RAB", "name": "Tokua Airport (RAB)"},
            {"id": "TKN", "name": "Tokunoshima (TKN)"},
            {"id": "TKS", "name": "Tokushima (TKS)"},
            {"id": "HND", "name": "Tokyo Intl (HND)"},
            {"id": "FTU", "name": "Tolagnaro (FTU)"},
            {"id": "TOL", "name": "Toledo (TOL)"},
            {"id": "TOW", "name": "Toledo Airport (TOW)"},
            {"id": "TLE", "name": "Toliara (TLE)"},
            {"id": "OVB", "name": "Tolmachevo (OVB)"},
            {"id": "TMG", "name": "Tomanggong Airport (TMG)"},
            {"id": "TOM", "name": "Tombouctou (TOM)"},
            {"id": "TOF", "name": "Tomsk Bogashevo Airport (TOF)"},
            {"id": "TGU", "name": "Toncontin Intl (TGU)"},
            {"id": "TGO", "name": "Tongliao Airport (TGO)"},
            {"id": "TGH", "name": "Tongoa Island Airport (TGH)"},
            {"id": "TEN", "name": "Tongren (TEN)"},
            {"id": "TNX", "name": "Tonopah Test Range (TNX)"},
            {"id": "TWB", "name": "Toowoomba (TWB)"},
            {"id": "TRN", "name": "Torino (TRN)"},
            {"id": "TRF", "name": "Torp (TRF)"},
            {"id": "TOJ", "name": "Torrejon (TOJ)"},
            {"id": "TRC", "name": "Torreon Intl (TRC)"},
            {"id": "TOH", "name": "Torres Airstrip (TOH)"},
            {"id": "TYF", "name": "Torsby Airport (TYF)"},
            {"id": "TTB", "name": "Tortoli (TTB)"},
            {"id": "GMR", "name": "Totegegie (GMR)"},
            {"id": "TOT", "name": "Totness Airstrip (TOT)"},
            {"id": "TTJ", "name": "Tottori (TTJ)"},
            {"id": "AZR", "name": "Touat Cheikh Sidi Mohamed Belkebir (AZR)"},
            {"id": "TOU", "name": "Touho (TOU)"},
            {"id": "PAP", "name": "Toussaint Louverture Intl (PAP)"},
            {"id": "TNF", "name": "Toussus Le Noble (TNF)"},
            {"id": "TSV", "name": "Townsville (TSV)"},
            {"id": "TOY", "name": "Toyama (TOY)"},
            {"id": "TZX", "name": "Trabzon (TZX)"},
            {"id": "TSR", "name": "Traian Vuia (TSR)"},
            {"id": "YZZ", "name": "Trail Airport (YZZ)"},
            {"id": "XAD", "name": "Train Station (XAD)"},
            {"id": "XDS", "name": "Train Station (XDS)"},
            {"id": "XEF", "name": "Train Station (XEF)"},
            {"id": "XVV", "name": "Train Station (XVV)"},
            {"id": "XZL", "name": "Train Station (XZL)"},
            {"id": "ZRD", "name": "Train Station (ZRD)"},
            {"id": "TST", "name": "Trang (TST)"},
            {"id": "TGM", "name": "Transilvania Targu Mures (TGM)"},
            {"id": "TPS", "name": "Trapani Birgi (TPS)"},
            {"id": "TDX", "name": "Trat (TDX)"},
            {"id": "SUU", "name": "Travis Afb (SUU)"},
            {"id": "TCB", "name": "Treasure Cay (TCB)"},
            {"id": "YTR", "name": "Trenton (YTR)"},
            {"id": "TTN", "name": "Trenton Mercer (TTN)"},
            {"id": "N87", "name": "Trenton-Robbinsville Airport (N87)"},
            {"id": "PUU", "name": "Tres De Mayo (PUU)"},
            {"id": "TSO", "name": "Tresco Heliport (TSO)"},
            {"id": "TLG", "name": "Treuchtlingen BF (TLG)"},
            {"id": "TSF", "name": "Treviso (TSF)"},
            {"id": "PSC", "name": "Tri Cities Airport (PSC)"},
            {"id": "HTS", "name": "Tri State Milton J Ferguson Field (HTS)"},
            {"id": "CZG", "name": "Tri-Cities (CZG)"},
            {"id": "TRI", "name": "Tri-Cities Regional Airport (TRI)"},
            {"id": "LNR", "name": "Tri-County Regional Airport (LNR)"},
            {"id": "ANQ", "name": "Tri-State Steuben County Airport (ANQ)"},
            {"id": "KTM", "name": "Tribhuvan Intl (KTM)"},
            {"id": "TRZ", "name": "Trichy (TRZ)"},
            {"id": "ZQF", "name": "Trier Fohren (ZQF)"},
            {"id": "TIP", "name": "Tripoli Intl (TIP)"},
            {"id": "YRQ", "name": "Trois Rivieres Airport (YRQ)"},
            {"id": "THN", "name": "Trollhattan Vanersborg (THN)"},
            {"id": "TMT", "name": "Trombetas (TMT)"},
            {"id": "TKF", "name": "Truckee-Tahoe Airport (TKF)"},
            {"id": "TCS", "name": "Truth Or Consequences Muni (TCS)"},
            {"id": "VCA", "name": "Trà Nóc Airport (VCA)"},
            {"id": "TTS", "name": "Tsaratanana Airport (TTS)"},
            {"id": "TSH", "name": "Tshikapa Airport (TSH)"},
            {"id": "MAT", "name": "Tshimpi Airport (MAT)"},
            {"id": "WTS", "name": "Tsiroanomandidy Airport (WTS)"},
            {"id": "QDJ", "name": "Tsletsi Airport (QDJ)"},
            {"id": "CNW", "name": "Tstc Waco (CNW)"},
            {"id": "TSB", "name": "Tsumeb Airport (TSB)"},
            {"id": "TSJ", "name": "Tsushima (TSJ)"},
            {"id": "TDD", "name": "Tte Av Jorge Henrich Arauz (TDD)"},
            {"id": "PSZ", "name": "Tte De Av Salvador Ogaya G (PSZ)"},
            {"id": "TUB", "name": "Tubuai (TUB)"},
            {"id": "TUW", "name": "Tubuala Airport (TUW)"},
            {"id": "TUS", "name": "Tucson Intl (TUS)"},
            {"id": "TCC", "name": "Tucumcari Muni (TCC)"},
            {"id": "TUV", "name": "Tucupita (TUV)"},
            {"id": "TUR", "name": "Tucurui (TUR)"},
            {"id": "TFI", "name": "Tufi Airport (TFI)"},
            {"id": "TUG", "name": "Tuguegarao Airport (TUG)"},
            {"id": "YUB", "name": "Tuktoyaktuk (YUB)"},
            {"id": "TYA", "name": "Tula (TYA)"},
            {"id": "BIV", "name": "Tulip City Airport (BIV)"},
            {"id": "ZFN", "name": "Tulita (ZFN)"},
            {"id": "TUL", "name": "Tulsa Intl (TUL)"},
            {"id": "DNP", "name": "Tulsipur (DNP)"},
            {"id": "TLT", "name": "Tuluksak Airport (TLT)"},
            {"id": "TMI", "name": "Tumling Tar (TMI)"},
            {"id": "CXP", "name": "Tunggul Wulung (CXP)"},
            {"id": "UTM", "name": "Tunica Municipal Airport (UTM)"},
            {"id": "IAR", "name": "Tunoshna (IAR)"},
            {"id": "WTL", "name": "Tuntutuliak Airport (WTL)"},
            {"id": "TNK", "name": "Tununak Airport (TNK)"},
            {"id": "TXN", "name": "Tunxi International Airport (TXN)"},
            {"id": "TUP", "name": "Tupelo Regional Airport (TUP)"},
            {"id": "TUI", "name": "Turaif (TUI)"},
            {"id": "BRQ", "name": "Turany (BRQ)"},
            {"id": "TUK", "name": "Turbat International Airport (TUK)"},
            {"id": "ZTA", "name": "Tureia Airport (ZTA)"},
            {"id": "CRZ", "name": "Turkmenabat (CRZ)"},
            {"id": "KRW", "name": "Turkmenbashi (KRW)"},
            {"id": "TKU", "name": "Turku (TKU)"},
            {"id": "TLQ", "name": "Turpan (TLQ)"},
            {"id": "TCL", "name": "Tuscaloosa Rgnl (TCL)"},
            {"id": "HVN", "name": "Tweed-New Haven Airport (HVN)"},
            {"id": "ENS", "name": "Twenthe (ENS)"},
            {"id": "NXP", "name": "Twentynine Palms Eaf (NXP)"},
            {"id": "TWA", "name": "Twin Hills Airport (TWA)"},
            {"id": "TYR", "name": "Tyler Pounds Rgnl (TYR)"},
            {"id": "PAM", "name": "Tyndall Afb (PAM)"},
            {"id": "TYE", "name": "Tyonek Airport (TYE)"},
            {"id": "LTA", "name": "Tzaneen (LTA)"},
            {"id": "ZTB", "name": "Tête-à-la-Baleine Airport (ZTB)"},
            {"id": "UTP", "name": "U Taphao Intl (UTP)"},
            {"id": "UAH", "name": "Ua Huka Airport (UAH)"},
            {"id": "UAP", "name": "Ua Pou Airport (UAP)"},
            {"id": "QUB", "name": "Ubari Airport (QUB)"},
            {"id": "UBA", "name": "Uberaba (UBA)"},
            {"id": "UBP", "name": "Ubon Ratchathani (UBP)"},
            {"id": "UDR", "name": "Udaipur (UDR)"},
            {"id": "UTH", "name": "Udon Thani (UTH)"},
            {"id": "UFA", "name": "Ufa (UFA)"},
            {"id": "UUK", "name": "Ugnu-Kuparuk Airport (UUK)"},
            {"id": "DYR", "name": "Ugolny Airport (DYR)"},
            {"id": "UGO", "name": "Uige (UGO)"},
            {"id": "UJE", "name": "Ujae Atoll Airport (UJE)"},
            {"id": "UCT", "name": "Ukhta (UCT)"},
            {"id": "UKA", "name": "Ukunda Airport (UKA)"},
            {"id": "ULO", "name": "Ulaangom Airport (ULO)"},
            {"id": "HLH", "name": "Ulanhot Airport (HLH)"},
            {"id": "RNA", "name": "Ulawa Airport (RNA)"},
            {"id": "ULI", "name": "Ulithi (ULI)"},
            {"id": "USN", "name": "Ulsan (USN)"},
            {"id": "YHI", "name": "Ulukhaktok Holman (YHI)"},
            {"id": "ULX", "name": "Ulusaba Airstrip (ULX)"},
            {"id": "ULY", "name": "Ulyanovsk East Airport (ULY)"},
            {"id": "ULB", "name": "Uléi Airport (ULB)"},
            {"id": "UME", "name": "Umea (UME)"},
            {"id": "YUD", "name": "Umiujaq Airport (YUD)"},
            {"id": "UNA", "name": "Una-Comandatuba Airport (UNA)"},
            {"id": "UNK", "name": "Unalakleet Airport (UNK)"},
            {"id": "DUT", "name": "Unalaska (DUT)"},
            {"id": "UNI", "name": "Union Island International Airport (UNI)"},
            {"id": "YBZ", "name": "Union Station (YBZ)"},
            {"id": "SCE", "name": "University Park Airport (SCE)"},
            {"id": "UNT", "name": "Unst Airport (UNT)"},
            {"id": "JUV", "name": "Upernavik Airport (JUV)"},
            {"id": "UTN", "name": "Upington (UTN)"},
            {"id": "UPP", "name": "Upolu (UPP)"},
            {"id": "URJ", "name": "Uraj (URJ)"},
            {"id": "URA", "name": "Uralsk (URA)"},
            {"id": "YBE", "name": "Uranium City Airport (YBE)"},
            {"id": "UGC", "name": "Urgench Airport (UGC)"},
            {"id": "OMH", "name": "Uromiyeh Airport (OMH)"},
            {"id": "ATD", "name": "Uru Harbour Airport (ATD)"},
            {"id": "USQ", "name": "Usak Airport (USQ)"},
            {"id": "USH", "name": "Ushuaia Malvinas Argentinas (USH)"},
            {"id": "IPN", "name": "Usiminas (IPN)"},
            {"id": "USK", "name": "Usinsk (USK)"},
            {"id": "UKK", "name": "Ust Kamenogorsk Airport (UKK)"},
            {"id": "UIK", "name": "Ust-Ilimsk (UIK)"},
            {"id": "UKX", "name": "Ust-Kut (UKX)"},
            {"id": "UMS", "name": "Ust-Maya Airport (UMS)"},
            {"id": "UTU", "name": "Ustupo (UTU)"},
            {"id": "UII", "name": "Utila Airport (UII)"},
            {"id": "UTK", "name": "Utirik Airport (UTK)"},
            {"id": "QVY", "name": "Utti (QVY)"},
            {"id": "UMD", "name": "Uummannaq Heliport (UMD)"},
            {"id": "MCX", "name": "Uytash (MCX)"},
            {"id": "UYU", "name": "Uyuni Airport (UYU)"},
            {"id": "UDJ", "name": "Uzhhorod International Airport (UDJ)"},
            {"id": "HKV", "name": "Uzundzhovo (HKV)"},
            {"id": "KFS", "name": "Uzunyazi (KFS)"},
            {"id": "ANU", "name": "V C Bird Intl (ANU)"},
            {"id": "VAA", "name": "Vaasa (VAA)"},
            {"id": "BDQ", "name": "Vadodara (BDQ)"},
            {"id": "TRD", "name": "Vaernes (TRD)"},
            {"id": "FAE", "name": "Vagar (FAE)"},
            {"id": "YVO", "name": "Val D Or (YVO)"},
            {"id": "BEL", "name": "Val De Cans Intl (BEL)"},
            {"id": "TUF", "name": "Val De Loire (TUF)"},
            {"id": "HVG", "name": "Valan (HVG)"},
            {"id": "VDZ", "name": "Valdez Pioneer Fld (VDZ)"},
            {"id": "VLD", "name": "Valdosta Regional Airport (VLD)"},
            {"id": "VAL", "name": "Valenca Airport (VAL)"},
            {"id": "VLC", "name": "Valencia (VLC)"},
            {"id": "VLS", "name": "Valesdir Airport (VLS)"},
            {"id": "X59", "name": "Valkaria Municipal (X59)"},
            {"id": "LID", "name": "Valkenburg (LID)"},
            {"id": "VLL", "name": "Valladolid (VLL)"},
            {"id": "VDP", "name": "Valle De La Pascua (VDP)"},
            {"id": "LMM", "name": "Valle Del Fuerte Intl (LMM)"},
            {"id": "URO", "name": "Vallee De Seine (URO)"},
            {"id": "HRL", "name": "Valley Intl (HRL)"},
            {"id": "VAN", "name": "Van (VAN)"},
            {"id": "VNY", "name": "Van Nuys (VNY)"},
            {"id": "VNW", "name": "Van Wert County Airport (VNW)"},
            {"id": "END", "name": "Vance Afb (END)"},
            {"id": "NEV", "name": "Vance Winkworth Amory International Airport (NEV)"},
            {"id": "CXH", "name": "Vancouver Coal Harbour (CXH)"},
            {"id": "YHC", "name": "Vancouver Harbour Water Airport (YHC)"},
            {"id": "YVR", "name": "Vancouver Intl (YVR)"},
            {"id": "VBG", "name": "Vandenberg Afb (VBG)"},
            {"id": "VVC", "name": "Vanguardia (VVC)"},
            {"id": "VAI", "name": "Vanimo Airport (VAI)"},
            {"id": "VBV", "name": "Vanua Balavu Airport (VBV)"},
            {"id": "VNS", "name": "Varanasi (VNS)"},
            {"id": "VRK", "name": "Varkaus (VRK)"},
            {"id": "VAR", "name": "Varna (VAR)"},
            {"id": "VST", "name": "Vasteras (VST)"},
            {"id": "XCR", "name": "Vatry (XCR)"},
            {"id": "VAV", "name": "Vavau Intl (VAV)"},
            {"id": "VLU", "name": "Velikiye Luki (VLU)"},
            {"id": "VUS", "name": "Veliky Ustyug (VUS)"},
            {"id": "VEE", "name": "Venetie Airport (VEE)"},
            {"id": "VCE", "name": "Venezia Tessera (VCE)"},
            {"id": "VTS", "name": "Ventspils International Airport (VTS)"},
            {"id": "JUI", "name": "Verkehrslandeplatz Juist (JUI)"},
            {"id": "YVG", "name": "Vermilion (YVG)"},
            {"id": "DNV", "name": "Vermilion Regional (DNV)"},
            {"id": "VEL", "name": "Vernal Regional Airport (VEL)"},
            {"id": "VRB", "name": "Vero Beach Muni (VRB)"},
            {"id": "VEY", "name": "Vestmannaeyjar (VEY)"},
            {"id": "BZR", "name": "Vias (BZR)"},
            {"id": "VIC", "name": "Vicenza (VIC)"},
            {"id": "VFA", "name": "Victoria Falls Intl (VFA)"},
            {"id": "YWH", "name": "Victoria Inner Harbour Airport (YWH)"},
            {"id": "YYJ", "name": "Victoria Intl (YYJ)"},
            {"id": "VCT", "name": "Victoria Regional Airport (VCT)"},
            {"id": "VCD", "name": "Victoria River Downs Airport (VCD)"},
            {"id": "VQS", "name": "Vieques Airport (VQS)"},
            {"id": "VIF", "name": "Vieste Heliport (VIF)"},
            {"id": "VGO", "name": "Vigo (VGO)"},
            {"id": "AES", "name": "Vigra (AES)"},
            {"id": "VGA", "name": "Vijayawada (VGA)"},
            {"id": "VRL", "name": "Vila Real (VRL)"},
            {"id": "VNX", "name": "Vilankulo (VNX)"},
            {"id": "VHM", "name": "Vilhelmina (VHM)"},
            {"id": "BVH", "name": "Vilhena (BVH)"},
            {"id": "VDR", "name": "Villa Dolores (VDR)"},
            {"id": "VGZ", "name": "Villa Garzon Airport (VGZ)"},
            {"id": "VLG", "name": "Villa Gesell (VLG)"},
            {"id": "VRN", "name": "Villafranca (VRN)"},
            {"id": "VNO", "name": "Vilnius Intl (VNO)"},
            {"id": "CYO", "name": "Vilo Acuna Intl (CYO)"},
            {"id": "OEM", "name": "Vincent Fayks Airport (OEM)"},
            {"id": "VII", "name": "Vinh Airport (VII)"},
            {"id": "VIN", "name": "Vinnitsa (VIN)"},
            {"id": "VRC", "name": "Virac Airport (VRC)"},
            {"id": "VCP", "name": "Viracopos (VCP)"},
            {"id": "VIJ", "name": "Virgin Gorda Airport (VIJ)"},
            {"id": "VIR", "name": "Virginia (VIR)"},
            {"id": "VVI", "name": "Viru Viru Intl (VVI)"},
            {"id": "VIS", "name": "Visalia Municipal Airport (VIS)"},
            {"id": "VBY", "name": "Visby (VBY)"},
            {"id": "VTZ", "name": "Vishakhapatnam (VTZ)"},
            {"id": "VTB", "name": "Vitebsk (VTB)"},
            {"id": "VIT", "name": "Vitoria (VIT)"},
            {"id": "AAQ", "name": "Vityazevo (AAQ)"},
            {"id": "VDC", "name": "Vitória da Conquista Airport (VDC)"},
            {"id": "VKO", "name": "Vnukovo (VKO)"},
            {"id": "VOH", "name": "Vohimarina (VOH)"},
            {"id": "VOK", "name": "Volk Fld (VOK)"},
            {"id": "UDE", "name": "Volkel AB (UDE)"},
            {"id": "VGD", "name": "Vologda Airport (VGD)"},
            {"id": "VPN", "name": "Vopnafjörður Airport (VPN)"},
            {"id": "VKT", "name": "Vorkuta Airport (VKT)"},
            {"id": "VRU", "name": "Vryburg (VRU)"},
            {"id": "KDV", "name": "Vunisea Airport (KDV)"},
            {"id": "VRY", "name": "Værøy Heliport (VRY)"},
            {"id": "YWK", "name": "Wabush (YWK)"},
            {"id": "ACT", "name": "Waco Rgnl (ACT)"},
            {"id": "WTN", "name": "Waddington (WTN)"},
            {"id": "EWD", "name": "Wadi Al Dawasir Airport (EWD)"},
            {"id": "WHF", "name": "Wadi Halfa Airport (WHF)"},
            {"id": "3G3", "name": "Wadsworth Municipal (3G3)"},
            {"id": "AGI", "name": "Wageningen Airstrip (AGI)"},
            {"id": "WGA", "name": "Wagga Wagga (WGA)"},
            {"id": "MOF", "name": "Wai Oti (MOF)"},
            {"id": "SWA", "name": "Wai Sha Airport (SWA)"},
            {"id": "WKL", "name": "Waikoloa Heliport (WKL)"},
            {"id": "MUE", "name": "Waimea Kohala (MUE)"},
            {"id": "AIN", "name": "Wainwright Airport (AIN)"},
            {"id": "K03", "name": "Wainwright As (K03)"},
            {"id": "WJR", "name": "Wajir (WJR)"},
            {"id": "AWK", "name": "Wake Island Afld (AWK)"},
            {"id": "WKJ", "name": "Wakkanai (WKJ)"},
            {"id": "WLH", "name": "Walaha Airport (WLH)"},
            {"id": "WAA", "name": "Wales Airport (WAA)"},
            {"id": "WGE", "name": "Walgett Airport (WGE)"},
            {"id": "JRB", "name": "Wall Street Heliport (JRB)"},
            {"id": "ALW", "name": "Walla Walla Regional Airport (ALW)"},
            {"id": "AXA", "name": "Wallblake (AXA)"},
            {"id": "WLS", "name": "Wallis (WLS)"},
            {"id": "WAL", "name": "Wallops Flight Facility (WAL)"},
            {"id": "BWF", "name": "Walney Island (BWF)"},
            {"id": "WVB", "name": "Walvis Bay Airport (WVB)"},
            {"id": "WMX", "name": "Wamena (WMX)"},
            {"id": "WKA", "name": "Wanaka (WKA)"},
            {"id": "WOT", "name": "Wang An (WOT)"},
            {"id": "WAG", "name": "Wanganui (WAG)"},
            {"id": "AGE", "name": "Wangerooge Airport (AGE)"},
            {"id": "AGL", "name": "Wanigela Airport (AGL)"},
            {"id": "WXN", "name": "Wanxian Airport (WXN)"},
            {"id": "YAX", "name": "Wapekeka Airport (YAX)"},
            {"id": "WBM", "name": "Wapenamanda Airport (WBM)"},
            {"id": "WAR", "name": "Waris Airport (WAR)"},
            {"id": "SYU", "name": "Warraber Island Airport (SYU)"},
            {"id": "QRW", "name": "Warri Airport (QRW)"},
            {"id": "WMI", "name": "Warsaw Modlin (WMI)"},
            {"id": "TJG", "name": "Warukin Airport (TJG)"},
            {"id": "IAD", "name": "Washington Dulles Intl (IAD)"},
            {"id": "ZWU", "name": "Washington Union Station (ZWU)"},
            {"id": "YKQ", "name": "Waskaganish Airport (YKQ)"},
            {"id": "WSP", "name": "Waspam Airport (WSP)"},
            {"id": "OXC", "name": "Waterbury-Oxford Airport (OXC)"},
            {"id": "WAT", "name": "Waterford (WAT)"},
            {"id": "YKF", "name": "Waterloo (YKF)"},
            {"id": "QQW", "name": "Waterloo International (QQW)"},
            {"id": "ALO", "name": "Waterloo Regional Airport (ALO)"},
            {"id": "ART", "name": "Watertown Intl (ART)"},
            {"id": "ATY", "name": "Watertown Regional Airport (ATY)"},
            {"id": "YQH", "name": "Watson Lake (YQH)"},
            {"id": "VTE", "name": "Wattay Intl (VTE)"},
            {"id": "WUU", "name": "Wau Airport (WUU)"},
            {"id": "UGN", "name": "Waukegan Rgnl (UGN)"},
            {"id": "UES", "name": "Waukesha County Airport (UES)"},
            {"id": "PCZ", "name": "Waupaca Municipal Airport (PCZ)"},
            {"id": "AUW", "name": "Wausau Downtown Airport (AUW)"},
            {"id": "YXZ", "name": "Wawa (YXZ)"},
            {"id": "TBN", "name": "Waynesville Rgnl Arpt At Forney Fld (TBN)"},
            {"id": "YWP", "name": "Webequie Airport (YWP)"},
            {"id": "EUF", "name": "Weedon Field (EUF)"},
            {"id": "WEF", "name": "Weifang Airport (WEF)"},
            {"id": "WEH", "name": "Weihai Airport (WEH)"},
            {"id": "WEI", "name": "Weipa (WEI)"},
            {"id": "EJH", "name": "Wejh (EJH)"},
            {"id": "6Y8", "name": "Welke Airport (6Y8)"},
            {"id": "WEL", "name": "Welkom (WEL)"},
            {"id": "WLG", "name": "Wellington Intl (WLG)"},
            {"id": "EGT", "name": "Wellington Municipal (EGT)"},
            {"id": "YNC", "name": "Wemindji Airport (YNC)"},
            {"id": "ENV", "name": "Wendover (ENV)"},
            {"id": "WNH", "name": "Wenshan Airport (WNH)"},
            {"id": "WNZ", "name": "Wenzhou Yongqiang Airport (WNZ)"},
            {"id": "JRA", "name": "West 30th St. Heliport (JRA)"},
            {"id": "CTJ", "name": "West Georgia Regional Airport - O V Gray Field (CTJ)"},
            {"id": "IWS", "name": "West Houston (IWS)"},
            {"id": "KWP", "name": "West Point Village Seaplane Base (KWP)"},
            {"id": "XWW", "name": "Westbahnhoff (XWW)"},
            {"id": "HPN", "name": "Westchester Co (HPN)"},
            {"id": "GWT", "name": "Westerland Sylt (GWT)"},
            {"id": "WST", "name": "Westerly State Airport (WST)"},
            {"id": "BFF", "name": "Western Nebraska Regional Airport (BFF)"},
            {"id": "CEF", "name": "Westover Arb Metropolitan (CEF)"},
            {"id": "WSZ", "name": "Westport (WSZ)"},
            {"id": "WRY", "name": "Westray Airport (WRY)"},
            {"id": "WSX", "name": "Westsound Seaplane Base (WSX)"},
            {"id": "QKT", "name": "Wevelgem (QKT)"},
            {"id": "WWK", "name": "Wewak Intl (WWK)"},
            {"id": "WHK", "name": "Whakatane (WHK)"},
            {"id": "YXN", "name": "Whale Cove Airport (YXN)"},
            {"id": "WRE", "name": "Whangarei (WRE)"},
            {"id": "YLE", "name": "Whatì Airport (YLE)"},
            {"id": "HHI", "name": "Wheeler Aaf (HHI)"},
            {"id": "GTB", "name": "Wheeler Sack Aaf (GTB)"},
            {"id": "HLG", "name": "Wheeling Ohio County Airport (HLG)"},
            {"id": "NUW", "name": "Whidbey Island Nas (NUW)"},
            {"id": "YWS", "name": "Whistler/Green Lake Water Aerodrome (YWS)"},
            {"id": "WMO", "name": "White Mountain Airport (WMO)"},
            {"id": "YZU", "name": "Whitecourt (YZU)"},
            {"id": "YXY", "name": "Whitehorse Intl (YXY)"},
            {"id": "SZL", "name": "Whiteman Afb (SZL)"},
            {"id": "WHP", "name": "Whiteman Airport (WHP)"},
            {"id": "WTZ", "name": "Whitianga Airport (WTZ)"},
            {"id": "NSE", "name": "Whiting Fld Nas North (NSE)"},
            {"id": "WSY", "name": "Whitsunday Airstrip (WSY)"},
            {"id": "WLF", "name": "Whittlesford Parkway Rail Station (WLF)"},
            {"id": "WYA", "name": "Whyalla Airport (WYA)"},
            {"id": "YVV", "name": "Wiarton (YVV)"},
            {"id": "ICT", "name": "Wichita Mid Continent (ICT)"},
            {"id": "WIC", "name": "Wick (WIC)"},
            {"id": "E25", "name": "Wickenburg Municipal Airport (E25)"},
            {"id": "ASI", "name": "Wideawake Field (ASI)"},
            {"id": "WIO", "name": "Wilcannia (WIO)"},
            {"id": "BRW", "name": "Wiley Post Will Rogers Mem (BRW)"},
            {"id": "WVN", "name": "Wilhelmshaven Mariensiel (WVN)"},
            {"id": "AVP", "name": "Wilkes Barre Scranton Intl (AVP)"},
            {"id": "WBW", "name": "Wilkes-Barre Wyoming Valley Airport (WBW)"},
            {"id": "OKC", "name": "Will Rogers World (OKC)"},
            {"id": "HOU", "name": "William P Hobby (HOU)"},
            {"id": "CLM", "name": "William R Fairchild International Airport (CLM)"},
            {"id": "LHV", "name": "William T. Piper Mem. (LHV)"},
            {"id": "0G6", "name": "Williams County Airport (0G6)"},
            {"id": "YWM", "name": "Williams Harbour Airport (YWM)"},
            {"id": "YWL", "name": "Williams Lake (YWL)"},
            {"id": "MWA", "name": "Williamson Country Regional Airport (MWA)"},
            {"id": "SDC", "name": "Williamson-Sodus Airport (SDC)"},
            {"id": "IPT", "name": "Williamsport Rgnl (IPT)"},
            {"id": "NXX", "name": "Willow Grove Nas Jrb (NXX)"},
            {"id": "YIP", "name": "Willow Run (YIP)"},
            {"id": "ILN", "name": "Wilmington Airborne Airpark (ILN)"},
            {"id": "ZWI", "name": "Wilmington Amtrak Station (ZWI)"},
            {"id": "ILM", "name": "Wilmington Intl (ILM)"},
            {"id": "WUN", "name": "Wiluna Airport (WUN)"},
            {"id": "IJD", "name": "Windham Airport (IJD)"},
            {"id": "WDH", "name": "Windhoek Hosea Kutako International Airport  (WDH)"},
            {"id": "MWM", "name": "Windom Municipal Airport (MWM)"},
            {"id": "WNR", "name": "Windorah Airport (WNR)"},
            {"id": "YQG", "name": "Windsor (YQG)"},
            {"id": "BBX", "name": "Wings Field (BBX)"},
            {"id": "INK", "name": "Winkler Co (INK)"},
            {"id": "YWG", "name": "Winnipeg Intl (YWG)"},
            {"id": "YAV", "name": "Winnipeg St Andrews (YAV)"},
            {"id": "INW", "name": "Winslow-Lindbergh Regional Airport (INW)"},
            {"id": "WIN", "name": "Winton Airport (WIN)"},
            {"id": "WPM", "name": "Wipim Airport (WPM)"},
            {"id": "WRZ", "name": "Wirawila Airport (WRZ)"},
            {"id": "WIH", "name": "Wishram Amtrak Station (WIH)"},
            {"id": "SUA", "name": "Witham Field Airport (SUA)"},
            {"id": "OSH", "name": "Wittman Regional Airport (OSH)"},
            {"id": "WOE", "name": "Woensdrecht (WOE)"},
            {"id": "KLU", "name": "Woerthersee International Airport (KLU)"},
            {"id": "WTP", "name": "Woitape Airport (WTP)"},
            {"id": "WJA", "name": "Woja Airport (WJA)"},
            {"id": "GGW", "name": "Wokal Field Glasgow International Airport (GGW)"},
            {"id": "ZWL", "name": "Wollaston Lake Airport (ZWL)"},
            {"id": "WOL", "name": "Wollongong Airport (WOL)"},
            {"id": "KDI", "name": "Wolter Monginsidi (KDI)"},
            {"id": "PRY", "name": "Wonderboom (PRY)"},
            {"id": "WJU", "name": "Wonju Airport (WJU)"},
            {"id": "BHE", "name": "Woodbourne (BHE)"},
            {"id": "CDN", "name": "Woodward Field (CDN)"},
            {"id": "UMR", "name": "Woomera (UMR)"},
            {"id": "ORH", "name": "Worcester Regional Airport (ORH)"},
            {"id": "WRL", "name": "Worland Municipal Airport (WRL)"},
            {"id": "WTO", "name": "Wotho Island Airport (WTO)"},
            {"id": "WTE", "name": "Wotje Atoll Airport (WTE)"},
            {"id": "WRG", "name": "Wrangell Airport (WRG)"},
            {"id": "FFO", "name": "Wright Patterson Afb (FFO)"},
            {"id": "YWY", "name": "Wrigley (YWY)"},
            {"id": "WUA", "name": "Wuhai (WUA)"},
            {"id": "KMG", "name": "Wujiaba (KMG)"},
            {"id": "WNN", "name": "Wunnumin Lake Airport (WNN)"},
            {"id": "WZB", "name": "Wurzburg HBF (WZB)"},
            {"id": "TYN", "name": "Wusu (TYN)"},
            {"id": "WUX", "name": "Wuxi Airport (WUX)"},
            {"id": "NNG", "name": "Wuxu (NNG)"},
            {"id": "OHR", "name": "Wyk auf Foehr (OHR)"},
            {"id": "BWT", "name": "Wynyard Airport (BWT)"},
            {"id": "XGN", "name": "Xangongo (XGN)"},
            {"id": "GZM", "name": "Xewkija Heliport (GZM)"},
            {"id": "XFN", "name": "Xiangfan Airport (XFN)"},
            {"id": "XIY", "name": "Xianyang (XIY)"},
            {"id": "HGH", "name": "Xiaoshan (HGH)"},
            {"id": "XKH", "name": "Xieng Khouang (XKH)"},
            {"id": "XIL", "name": "Xilinhot Airport (XIL)"},
            {"id": "ACX", "name": "Xingyi Airport (ACX)"},
            {"id": "XNN", "name": "Xining Caojiabu Airport (XNN)"},
            {"id": "CGO", "name": "Xinzheng (CGO)"},
            {"id": "SIA", "name": "Xi'An Xiguan (SIA)"},
            {"id": "OAX", "name": "Xoxocotlan Intl (OAX)"},
            {"id": "XUZ", "name": "Xuzhou Guanyin Airport (XUZ)"},
            {"id": "BYC", "name": "Yacuiba (BYC)"},
            {"id": "DCY", "name": "Yading Daocheng (DCY)"},
            {"id": "CYT", "name": "Yakataga Airport (CYT)"},
            {"id": "YKM", "name": "Yakima Air Terminal McAllister Field (YKM)"},
            {"id": "JOS", "name": "Yakubu Gowon (JOS)"},
            {"id": "KUM", "name": "Yakushima (KUM)"},
            {"id": "YAK", "name": "Yakutat (YAK)"},
            {"id": "YKS", "name": "Yakutsk (YKS)"},
            {"id": "XMY", "name": "Yam Island Airport (XMY)"},
            {"id": "GAJ", "name": "Yamagata (GAJ)"},
            {"id": "UBJ", "name": "Yamaguchi Ube (UBJ)"},
            {"id": "ASK", "name": "Yamoussoukro (ASK)"},
            {"id": "HDN", "name": "Yampa Valley (HDN)"},
            {"id": "ENY", "name": "Yan'an Airport (ENY)"},
            {"id": "YNZ", "name": "Yancheng Airport (YNZ)"},
            {"id": "XYA", "name": "Yandina Airport (XYA)"},
            {"id": "RGN", "name": "Yangon Intl (RGN)"},
            {"id": "YNY", "name": "Yangyang International Airport (YNY)"},
            {"id": "YTY", "name": "Yangzhou Taizhou Airport (YTY)"},
            {"id": "YNJ", "name": "Yanji Airport (YNJ)"},
            {"id": "NSI", "name": "Yaounde Nsimalen (NSI)"},
            {"id": "YAO", "name": "Yaounde Ville (YAO)"},
            {"id": "YAP", "name": "Yap Intl (YAP)"},
            {"id": "EJA", "name": "Yariguies (EJA)"},
            {"id": "YQI", "name": "Yarmouth Airport (YQI)"},
            {"id": "GZA", "name": "Yasser Arafat Intl (GZA)"},
            {"id": "YES", "name": "Yasuj Airport (YES)"},
            {"id": "AZD", "name": "Yazd Shahid Sadooghi (AZD)"},
            {"id": "XYE", "name": "Ye (XYE)"},
            {"id": "CRW", "name": "Yeager (CRW)"},
            {"id": "YEC", "name": "Yecheon (YEC)"},
            {"id": "YLK", "name": "Yelahanka AFB (YLK)"},
            {"id": "PKC", "name": "Yelizovo (PKC)"},
            {"id": "YZF", "name": "Yellowknife (YZF)"},
            {"id": "WYS", "name": "Yellowstone Airport (WYS)"},
            {"id": "COD", "name": "Yellowstone Rgnl (COD)"},
            {"id": "YNB", "name": "Yenbo (YNB)"},
            {"id": "WYE", "name": "Yengema Airport (WYE)"},
            {"id": "YEI", "name": "Yenisehir Airport (YEI)"},
            {"id": "EIE", "name": "Yeniseysk Airport (EIE)"},
            {"id": "RSU", "name": "Yeosu (RSU)"},
            {"id": "YEO", "name": "Yeovilton (YEO)"},
            {"id": "YBP", "name": "Yibin (YBP)"},
            {"id": "YIH", "name": "Yichang Airport (YIH)"},
            {"id": "YIC", "name": "Yichun Mingyueshan Airport (YIC)"},
            {"id": "INC", "name": "Yinchuan (INC)"},
            {"id": "YIN", "name": "Yining Airport (YIN)"},
            {"id": "YIW", "name": "Yiwu Airport (YIW)"},
            {"id": "OKO", "name": "Yokota Ab (OKO)"},
            {"id": "YOL", "name": "Yola (YOL)"},
            {"id": "DWA", "name": "Yolo County Airport (DWA)"},
            {"id": "YGJ", "name": "Yonago Kitaro (YGJ)"},
            {"id": "OGN", "name": "Yonaguni (OGN)"},
            {"id": "LLF", "name": "Yongzhou Lingling Airport (LLF)"},
            {"id": "ZAC", "name": "York Landing Airport (ZAC)"},
            {"id": "OKR", "name": "Yorke Island Airport (OKR)"},
            {"id": "YQV", "name": "Yorkton Muni (YQV)"},
            {"id": "RNJ", "name": "Yoron (RNJ)"},
            {"id": "JOK", "name": "Yoshkar-Ola Airport (JOK)"},
            {"id": "4G4", "name": "Youngstown Elser Metro Airport (4G4)"},
            {"id": "YNG", "name": "Youngstown Warren Rgnl (YNG)"},
            {"id": "MYV", "name": "Yuba County Airport (MYV)"},
            {"id": "YUE", "name": "Yuendumu  (YUE)"},
            {"id": "UYN", "name": "Yulin Airport (UYN)"},
            {"id": "YUM", "name": "Yuma Mcas Yuma Intl (YUM)"},
            {"id": "YUS", "name": "Yushu Batang (YUS)"},
            {"id": "IWA", "name": "Yuzhny (IWA)"},
            {"id": "TAS", "name": "Yuzhny (TAS)"},
            {"id": "KZB", "name": "Zachar Bay Seaplane Base (KZB)"},
            {"id": "ZAD", "name": "Zadar (ZAD)"},
            {"id": "KZR", "name": "Zafer (KZR)"},
            {"id": "ZAG", "name": "Zagreb (ZAG)"},
            {"id": "ZAH", "name": "Zahedan Intl (ZAH)"},
            {"id": "ZAM", "name": "Zamboanga Intl (ZAM)"},
            {"id": "ZMM", "name": "Zamora (ZMM)"},
            {"id": "TOA", "name": "Zamperini Field Airport (TOA)"},
            {"id": "ZNZ", "name": "Zanzibar (ZNZ)"},
            {"id": "APZ", "name": "ZAPALA (APZ)"},
            {"id": "OZH", "name": "Zaporizhzhia International Airport (OZH)"},
            {"id": "ZTU", "name": "Zaqatala International Airport (ZTU)"},
            {"id": "ZAZ", "name": "Zaragoza Ab (ZAZ)"},
            {"id": "ZAJ", "name": "Zaranj Airport (ZAJ)"},
            {"id": "ZAR", "name": "Zaria (ZAR)"},
            {"id": "DJE", "name": "Zarzis (DJE)"},
            {"id": "TLM", "name": "Zenata (TLM)"},
            {"id": "YCU", "name": "Zhangxiao (YCU)"},
            {"id": "ZHA", "name": "Zhanjiang Airport (ZHA)"},
            {"id": "ZAT", "name": "Zhaotong Airport (ZAT)"},
            {"id": "DZN", "name": "Zhezkazgan Airport (DZN)"},
            {"id": "HJJ", "name": "Zhijiang Airport (HJJ)"},
            {"id": "PZH", "name": "Zhob (PZH)"},
            {"id": "ZGC", "name": "Zhongchuan (ZGC)"},
            {"id": "ZHY", "name": "Zhongwei Xiangshan Airport (ZHY)"},
            {"id": "JIQ", "name": "Zhoubai (JIQ)"},
            {"id": "HSN", "name": "Zhoushan Airport (HSN)"},
            {"id": "DLC", "name": "Zhoushuizi (DLC)"},
            {"id": "ZUH", "name": "Zhuhai Airport (ZUH)"},
            {"id": "IEV", "name": "Zhuliany Intl (IEV)"},
            {"id": "ZTR", "name": "Zhytomyr (ZTR)"},
            {"id": "DAC", "name": "Zia Intl (DAC)"},
            {"id": "ZIG", "name": "Ziguinchor (ZIG)"},
            {"id": "ZND", "name": "Zinder (ZND)"},
            {"id": "IZA", "name": "Zona da Mata Regional Airport (IZA)"},
            {"id": "ONQ", "name": "Zonguldak (ONQ)"},
            {"id": "QWC", "name": "Zoo (QWC)"},
            {"id": "ORG", "name": "Zorg en Hoop Airport (ORG)"},
            {"id": "MCZ", "name": "Zumbi Dos Palmares (MCZ)"},
            {"id": "ZRH", "name": "Zurich (ZRH)"},
            {"id": "EVN", "name": "Zvartnots (EVN)"},
            {"id": "ZQW", "name": "Zweibruecken (ZQW)"},
            {"id": "ZKP", "name": "Zyryanka West Airport (ZKP)"},
            {"id": "IDY", "name": "Île d'Yeu Airport (IDY)"},
            {"id": "ILP", "name": "Île des Pins Airport (ILP)"},
            {"id": "OSD", "name": "Östersund Airport (OSD)"},
            {"id": "AGH", "name": "Ängelholm-Helsingborg Airport (AGH)"},
            {"id": "LCJ", "name": "�?ódź Władysław Reymont Airport (LCJ)"},
            {"id": "ILZ", "name": "Žilina Airport (ILZ)"}
        ];
        self.origin = [];
        self.destination =[];
        self.allContacts = contacts.map(function (contact) {
            contact.lowerCasedName = contact.name.toLowerCase();
            return contact;
        });

        self.filterSelected = true;

        var stateName = $state ? $state.$current.self.name : 'flights',
            setFlightsGrid = function (grid, response) {
                //NEEDED because java services responses not standardize should have Lola change and Amit revert to what he had;
                var data = stateName === 'queryFlights' ? response.data.result : response.data;
                grid.totalItems = data.totalFlights === -1 ? 0 : data.totalFlights;
                grid.data = data.flights;
            },
            flightDirections = [
                {label: 'Inbound', value: 'I'},
                {label: 'Outbound', value: 'O'},
                {label: 'Any', value: ''}
            ],
            getPage = function () {
                setFlightsGrid($scope.flightsGrid, flights || {flights: [], totalFlights: 0 });
            },
            update = function (data) {
                flights = data;
                getPage();
            },
            fetchMethods = {
                queryFlights: function () {
                    executeQueryService.queryFlights(postData).then(update);
                },
                flights: function () {
                    flightService.getFlights($scope.model).then(update);
                }
            },
            resolvePage = function () {
                // @Venu when service updated can refactor to populate chips based on array of ids
                self.origin = querySearch($scope.model.origins);
                self.destination = querySearch($scope.model.dests);
                fetchMethods[stateName]();
            };

        $scope.selectedFlight = $stateParams.flight;
        $scope.flightDirections = flightDirections;
        $scope.stateName = stateName;
        $scope.flightsGrid = {
            paginationPageSizes: [10, 15, 25],
            paginationPageSize: $scope.model.pageSize,
            paginationCurrentPage: $scope.model.pageNumber,
            useExternalPagination: true,
            useExternalSorting: true,
            useExternalFiltering: true,
            enableHorizontalScrollbar: 0,
            enableVerticalScrollbar: 0,
            enableColumnMenus: false,
            exporterCsvFilename: 'Flights.csv',

            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;

                gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                    if (sortColumns.length === 0) {
                        $scope.model.sort = null;
                    } else {
                        $scope.model.sort = [];
                        for (var i = 0; i < sortColumns.length; i++) {
                            $scope.model.sort.push({
                                column: sortColumns[i].name,
                                dir: sortColumns[i].sort.direction
                            });
                        }
                    }
                    resolvePage();
                });

                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    if ($scope.model.pageNumber !== newPage || $scope.model.pageSize !== pageSize) {
                        $scope.model.pageNumber = newPage;
                        $scope.model.pageSize = pageSize;
                        resolvePage();
                    }
                });
            }
        };

        $scope.flightsGrid.columnDefs = [
            {
                name: 'passengerCount',
                field: 'passengerCount',
                displayName: 'P',
                width: 100,
                enableFiltering: false,
                cellTemplate: '<a ui-sref="flightpax({id: row.entity.id, flightNumber: row.entity.fullFlightNumber, origin: row.entity.origin, destination: row.entity.destination, direction: row.entity.direction, eta: row.entity.eta.substring(0, 10), etd: row.entity.etd.substring(0, 10)})" href="#/flights/{{row.entity.id}}/{{row.entity.fullFlightNumber}}/{{row.entity.origin}}/{{row.entity.destination}}/{{row.entity.direction}}/{{row.entity.eta.substring(0, 10)}}/{{row.entity.etd.substring(0, 10);}}" class="md-primary md-button md-default-theme" >{{COL_FIELD}}</a>'
            },
            {
                name: 'ruleHitCount',
                displayName: 'H',
                width: 50,
                enableFiltering: false,
                cellClass: gridService.colorHits,
                sort: {
                    direction: uiGridConstants.DESC,
                    priority: 0
                }
            },
            {
                name: 'listHitCount',
                displayName: 'L',
                width: 50,
                enableFiltering: false,
                cellClass: gridService.colorHits,
                sort: {
                    direction: uiGridConstants.DESC,
                    priority: 1
                }
            },
            {
                name: 'fullFlightNumber',
                displayName: 'Flight',
                width: 70
            },
            {
                name: 'etaLocalTZ', displayName: 'ETA',
                sort: {
                    direction: uiGridConstants.DESC,
                    priority: 2
                }
            },
            {name: 'etdLocalTZ', displayName: 'ETD'},
            {name: 'origin'},
            {name: 'originCountry', displayName: 'Country'},
            {name: 'destination'},
            {name: 'destinationCountry', displayName: 'Country'}
        ];

        $scope.queryPassengersOnSelectedFlight = function (row_entity) {
            $state.go('passengers', {
                flightNumber: row_entity.flightNumber,
                origin: row_entity.origin,
                dest: row_entity.dest
            });
        };

        $scope.filter = function () {
            //temporary as flightService doesn't support multiple values yet
            $scope.model.origin = self.origin.length ? self.origin.map(returnObjectId)[0] : '';
            $scope.model.dest = self.destination ? self.destination.map(returnObjectId)[0] : '';
            resolvePage();
        };

        $scope.reset = function () {
            $scope.model.reset();
            resolvePage();
        };

        $scope.getTableHeight = function () {
            return gridService.calculateGridHeight($scope.flightsGrid.data.length);
        };
        resolvePage();
    });
}());
