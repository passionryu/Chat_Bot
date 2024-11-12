import React, {useEffect, useState} from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Image, ScrollView, Linking } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

const ReportScreen = ({ navigation }) => {
    const [currentDifficulty, setCurrentDifficulty] = useState('잠시만 기다려주세요...');
    const [currentEmotion, setCurrentEmotion] = useState('잠시만 기다려주세요...');
    const [aiAdvice, setAiAdvice] = useState('잠시만 기다려주세요...');
    const [videoLink1, setVideoLink1] = useState(null);
    const [videoLink2, setVideoLink2] = useState(null);

    useEffect(() => {
        // 서버에 GET 요청을 보내는 함수
        const fetchReportData = async () => {
        try {
            const tokenData = await AsyncStorage.getItem('Tokens');
            const parsedTokenData = tokenData ? JSON.parse(tokenData) : null;
            const accessToken = parsedTokenData?.accessToken;

            if (!accessToken) {
                console.error('Access token이 없습니다. 로그인 후 다시 시도하세요.');
                alert("로그인이 필요합니다.");
                return;
            }
            const response = await axios.get('http://ceprj.gachon.ac.kr:60016/chatbot/finish',
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    }
                  }
            );
            // console.log(response.data);
            console.log(response.data.currentDifficulty);
            console.log(response.data.currentEmotion);
            console.log(response.data.aiAdvice);
            console.log(response.data.video_link1);
            console.log(response.data.video_link2);

            setCurrentDifficulty(response.data.currentDifficulty || '데이터 없음');
            setCurrentEmotion(response.data.currentEmotion || '데이터 없음');
            setAiAdvice(response.data.aiAdvice || '데이터 없음');
            setVideoLink1(response.data.video_link1);
            setVideoLink2(response.data.video_link2);
        } catch (error) {
            console.error('데이터 가져오기 실패:', error);
        }
        };

        fetchReportData(); // 화면 진입 시 데이터 요청
    }, []);

    // if (!reportData) {
    //     // 데이터를 가져오기 전 로딩 상태 표시
    //     return (
    //     <View style={styles.loadingContainer}>
    //         <Text>로딩 중...</Text>
    //     </View>
    //     );
    // } else {

    // }

    return (
        <ScrollView style={styles.container}>
        <Text style={styles.header}>AI 상담사 챗봇의 레포트</Text>

        <View style={styles.section}>
            <Text style={styles.sectionTitle}>현재의 어려움</Text>
            <View style={styles.sectionContent}>
            <Text style={styles.sectionText}>{currentDifficulty}</Text>
            </View>
        </View>

        <View style={styles.section}>
            <Text style={styles.sectionTitle}>느꼈던 감정</Text>
            <View style={styles.sectionContent}>
            <Text style={styles.sectionText}>{currentEmotion}</Text>
            </View>
        </View>

        <View style={styles.section}>
            <Text style={styles.sectionTitle}>AI 상담사 조언</Text>
            <View style={styles.sectionContent}>
            <Text style={styles.sectionText}>{aiAdvice}</Text>
            </View>
        </View>

        <Text style={styles.sectionTitle}>AI 상담사 추천 컨텐츠</Text>
        <View style={styles.recommendations}>
            <View style={styles.recommendationCard}>
            <TouchableOpacity
            style={styles.linkButton}
            onPress={() => Linking.openURL(videoLink1)}
            >
                <Text style={styles.linkText}>{videoLink1}</Text>
            </TouchableOpacity>
            </View>
            <View style={styles.recommendationCard}>
            <TouchableOpacity
            style={styles.linkButton}
            onPress={() => Linking.openURL(videoLink2)}
            >
                <Text style={styles.linkText}>{videoLink2}</Text>
            </TouchableOpacity>
            </View>
        </View>
        </ScrollView>
    );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5F5F5',
    padding: 16,
  },
  header: {
    fontSize: 20,
    textAlign: 'center',
    marginBottom: 20,
    fontFamily: 'Paperlogy-7Bold'
  },
  section: {
    marginBottom: 16,
  },
  sectionTitle: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#555',
    marginBottom: 4,
  },
  sectionContent: {
    backgroundColor: '#FFFFFF',
    padding: 12,
    borderRadius: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2,
  },
  sectionText: {
    fontSize: 14,
    color: '#333',
  },
  recommendations: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 16,
  },
  recommendationCard: {
    width: '48%',
    backgroundColor: '#FFFFFF',
    borderRadius: 12,
    padding: 8,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2,
  },
  avatar: {
    width: 60,
    height: 60,
    borderRadius: 30,
    marginBottom: 8,
  },
  linkButton: {
    backgroundColor: '#F3E8FF',
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderRadius: 8,
    width: '100%',
    alignItems: 'center',
  },
  linkText: {
    color: '#555',
    fontWeight: 'bold',
  },
});

export default ReportScreen;
