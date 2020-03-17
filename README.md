    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        Observable.fromArray(1, 2, 3, 4, 5)
                .map(integer -> {
                    System.out.println("map: " + integer);
                    return null;
                })
                .buffer(Integer.MAX_VALUE)
                .subscribe(objects -> {
                    System.out.println("subscribe: " + objects);
                }, throwable -> {
                    System.out.println("subscribe: " + throwable);
                });

        List<BonusBean> bonusBeans = new ArrayList<>();
        bonusBeans.add(newBonusBean("10:00", "12:00", 1000));
        bonusBeans.add(newBonusBean("12:00", "18:00", 3000));
        bonusBeans.add(newBonusBean("18:00", "21:00", 2000));
        bonusBeans.add(newBonusBean("10:00", "21:00", 5000));
        bonusBeans.add(newBonusBean("12:00", "21:00", 6000));
        Collections.sort(bonusBeans, (o1, o2) -> {
            int i1 = o1.startTime.compareTo(o2.startTime);
            return (i1 == 0 ? o1.endTime.compareTo(o2.endTime) : i1);
        });

        Set<String> jTime = new TreeSet<>();
        for (BonusBean bonusBean : bonusBeans) {
            jTime.add(bonusBean.startTime.toString());
            jTime.add(bonusBean.endTime.toString());
        }

        Map<String, Integer> jTimeOfIndex = new TreeMap<>();
        int k = 0;
        for (String time : jTime) {
            jTimeOfIndex.put(time, k++);
        }

        Map<Integer, String> jIndexOfTime = new TreeMap<>();
        k = 0;
        for (String time : jTime) {
            jIndexOfTime.put(k++, time);
        }

        //dp(i-x,y-y)
        int[][] dp = new int[bonusBeans.size() + 1][jTime.size()];
        for (int i = 1; i < dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++) {
                //获取Y轴
                BonusBean bonusBean = bonusBeans.get(i - 1);
                //结束时间小于等于x轴时间
                if (compareTo(bonusBean.endTime.toString(), jIndexOfTime.get(j)) > 0) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    Integer __j = jTimeOfIndex.get(bonusBean.startTime.toString());
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][__j] + bonusBean.amount.intValue());
                }
            }
        }

        for (int i = 0; i < dp.length; i++) {
            System.out.println(Arrays.toString(dp[i]));
        }

        //回溯查找使用哪些方案
        int[] selected = new int[bonusBeans.size()];
        int j = jTime.size() - 1;
        for (int i = dp.length - 1; i >= 1; i--) {
            if (dp[i][j] == dp[i - 1][j]) {
                selected[i - 1] = 0;
            } else {
                selected[i - 1] = 1;
                BonusBean bonusBean = bonusBeans.get(i - 1);
                j = jTimeOfIndex.get(bonusBean.startTime.toString());
                System.out.println("  " + bonusBean.startTime.toString() + " - " + bonusBean.endTime.toString());
            }
        }

        System.out.println(Arrays.toString(selected));

        for (int i = 0; i < selected.length; i++) {
            if (selected[i] == 1) {
                BonusBean bonusBean = bonusBeans.get(i);
                System.out.println(" --> " + bonusBean.startTime.toString() + " - " + bonusBean.endTime.toString());
            }
        }
    }

    private long compareTo(String time1, String time2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.parse(time1).getTime() - sdf.parse(time2).getTime();
    }

    private BonusBean newBonusBean(String startTime, String endTime, int amount) throws ParseException {
        return new BonusBean(convertTime(startTime), convertTime(endTime), BigDecimal.valueOf(amount));
    }

    private Time convertTime(String HHmm) throws ParseException {
        return new Time(new SimpleDateFormat("HH:mm").parse(HHmm).getTime());
    }

    class BonusBean {
        Time startTime;
        Time endTime;
        BigDecimal amount;

        public BonusBean(Time startTime, Time endTime, BigDecimal amount) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.amount = amount;
        }
    }
