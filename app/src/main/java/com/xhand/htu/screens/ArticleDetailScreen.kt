package com.xhand.htu.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(onBack:()->Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "详情",
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(10.dp)
                            .clip(CircleShape)
                            .clickable {
                                onBack()
                            }
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(10.dp)
                            .clip(CircleShape)
                            .clickable {
                            }
                        )
                }
            )
        },
        modifier = Modifier.padding(PaddingValues())
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding))
        {
            LazyColumn(
                modifier = Modifier.padding(15.dp)

            ) {
                item {
                    Text(
                        text = "关于申报2024年度河南省高等学校重点科研项目计划基础研究专项的通知",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "发布时间:2023-09-27    浏览次数:1364",
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "\n各有关单位：\n" +
                                "\n" +
                                "为加强高校基础研究投入，持续提升原始创新能力，支撑“双一流”建设和内涵发展，省教育厅决定继续组织实施河南省高等 学校重点科研项目计划基础研究专项项目（以下简称“基础研究专项”），现将2024年度基础研究专项申报工作相关事项通知如下：\n" +
                                "\n" +
                                "一、支持措施\n" +
                                "\n" +
                                "1.加大支持力度，注重持续投入。基础研究专项组织实施遵循教育和科学规律，坚持稳定支持，营造宽松环境，使科研人员潜心、长期从事基础研究。基础研究专项实施周期为3年，每项省财政资助科研经费30万元，每年资助10万元，持续资助3年。\n" +
                                "\n" +
                                "2.突出优势领域，注重科研支撑。基础研究专项组织实施坚持扶强扶优扶特，重点支持具有较好学科优势和科研基础的项目研究，以高水平科学研究支撑高质量学科建设，提升基础研究创新能力和水平。\n" +
                                "\n" +
                                "3.强化目标导向，注重绩效考核。基础研究专项实施目标责任制管理，以计划任务书为主要考核依据，项目验收绩效考核突出者，给予持续滚动支持。\n" +
                                "\n" +
                                "4.开展试点改革，注重科研诚信。持续推进科研领域“放管服”改革，依托基础研究专项开展科研经费包干制试点，实行科研经费定额包干资助。在预算定额内，申请书及任务书均无需编制经费预算。赋予项目负责人经费支配权，根据实际使用情况编制项目经费决算。实行项目负责人科研诚信承诺制，承诺项目经费全部用于与本项目研究工作相关的支出。\n" +
                                "\n" +
                                "二、支持领域\n" +
                                "\n" +
                                "基础研究专项主要任务是面向基础学科发展前沿开展前瞻性、引领性和独创性基础理论研究，稳定支持一批具有较强创新实力的基础研究人才潜心探索，结合国家和我省战略需求，鼓励跨领域、跨学科交叉研究，促进我省优势学科发展，推动若干科学前沿或者符合我省战略需求的重要领域取得突破。2024年度基础研究专项重点围绕数理信息、生命健康、新材料、新能源、生物育种及安全工程等交叉科学领域领域组织实施。\n" +
                                "\n" +
                                "三、实施原则及申报条件\n" +
                                "\n" +
                                "基础研究专项是河南省高等学校重点科研项目计划的组成部分，其申请、评审、管理和资金使用按照《河南省高等学校重点科研项目管理办法（修订）》（教科技〔2019〕234号）等有关规定执行。所申报的项目及项目负责人应符合管理办法第六、七、八条规定，并同时满足以下要求：\n" +
                                "\n" +
                                "1.项目负责人须具有良好的科研作风学风和科学道德；\n" +
                                "\n" +
                                "2.项目负责人具有从事基础研究工作的经历，已取得比较突出的创新性成果，拟开展的研究工作创新性较强，且与我省经济社会发展结合较紧密；\n" +
                                "\n" +
                                "3.项目负责人不超过45周岁（1979年1月1日后出生），具有博士学位和副高级及以上专业技术职称；\n" +
                                "\n" +
                                "4.项目负责人当年度只能申请1个项目。河南省高校科技创新团队支持计划、河南省高校科技创新人才支持计划及本计划承担者，不在支持范围；主持在研河南省高等学校重点科研项目资助计划以及逾期未结项者不得申报。\n" +
                                "\n" +
                                "附管理办法第六、七、八条规定：\n" +
                                "\n" +
                                "第六条 所申报的项目应符合以下要求：\n" +
                                "\n" +
                                "1.项目选题应符合经济社会发展重大需求，具有重要的理论意义和应用价值，重点支持交叉学科和前沿学科探索研究；\n" +
                                "\n" +
                                "2.项目应以关键性科学问题为牵引，有创新的学术思想，合理可行的研究路线或技术方案，目标明确，重点突出，经费预算合理，提交预期成果和达到的学术水平、经济社会效益具有可考核性，鼓励多学科研究人员形成科研团队开展合作研究；\n" +
                                "\n" +
                                "3.项目申请资料齐全完备，申请内容真实可信；\n" +
                                "\n" +
                                "4.国家法律、法规限制的领域不得作为研究内容，涉密项目申请不予受理。\n" +
                                "\n" +
                                "第七条 项目承担高校和人员应符合下列条件：\n" +
                                "\n" +
                                "1.项目承担高校具有较好的研究基础设施和基本的研究条件（实验室、基本设备和图书资料等），保障项目组顺利开展研究工作；\n" +
                                "\n" +
                                "2.项目负责人应为我省高等院校在职教师或科研人员；\n" +
                                "\n" +
                                "3.    项目负责人职称应为中级及以上，学风端正，恪守科研诚信。重点支持中青年教师或研究人员主持项目研究；\n" +
                                "\n" +
                                "4.项目负责人应是该项目的主要提出者和设计者，了解与本项目有关的国内外研究动态，立足于项目研究的前沿，具备组织和领导完成该项目的能力和学术水平，具有完成项目的良好信誉；\n" +
                                "\n" +
                                "5.项目负责人和项目组成员具有较高的研究水平和充足的时间保证，须是能够实际参加该项目工作并具有完成该项目能力的教师和科研人员，年龄、职称结构合理，能够形成和谐稳定的研究团队；\n" +
                                "\n" +
                                "6.鼓励在校学生作为项目组成员参与项目研究工作。\n" +
                                "\n" +
                                "第八条 下列人员一般不得作为项目负责人进行申报，但可以作为项目组成员参与项目的实施：\n" +
                                "\n" +
                                "1.已主持本计划资助项目和省、部级及以上资助项目且未结项（题）者；\n" +
                                "\n" +
                                "2.计划外出学习或连续出国时间超过一年以上不能实际主持项目者；\n" +
                                "\n" +
                                "3.在科研工作中存在失信或违规行为者。\n" +
                                "\n" +
                                "四、申报方式及时间\n" +
                                "\n" +
                                "1．基础研究专项以学校为单位统一申报我校申报推荐指标2项，各相关单位择优推荐2项；\n" +
                                "\n" +
                                "2．申请人填写《河南省高等学校重点科研项目计划基础研究专项申请书》（附件1，以下简称“申请书”），须突出重点，简明扼要，双面打印，不得超过40个页码；\n" +
                                "\n" +
                                "3．申请人制作电子讲稿，格式为ppt文件（Office2017兼容格式，含语音讲解30M以内），时长不超过10分钟，以备评审使用。内容主要包括：个人基本情况、研究方向及研究特色、科研工作基础及创新成果、拟开展的研究及预期成果、支撑学科发展及人才培养等；\n" +
                                "\n" +
                                "4．每个项目报送纸质《申请书》2份，同时通过河南省高校科技管理云服务平台提交申请书和ppt电子文档。\n" +
                                "\n" +
                                "电子材料提交时间：2023年9月27日-10月13日18时；\n" +
                                "\n" +
                                "纸质材料受理时间：2023年10月13日。\n" +
                                "\n" +
                                "受理地点：致远楼516，联系人：郭志平，电话：3326160"
                    )
                }

            }

        }
    }
}