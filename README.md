# itSginX
        List<BonusBean> bonusBeans = new ArrayList<>();
        bonusBeans.add(newBonusBean("10:00", "12:00", 1000));
        bonusBeans.add(newBonusBean("12:00", "18:00", 3000));
        bonusBeans.add(newBonusBean("18:00", "21:00", 2000));
        bonusBeans.add(newBonusBean("10:00", "21:00", 7000));

        Set<Long> yTime = new TreeSet<>();
        for (BonusBean bonusBean : bonusBeans) {
            yTime.add(getTimeMillis(bonusBean.startTime));
            yTime.add(getTimeMillis(bonusBean.endTime));
        }

        //dp(i-x,y-y)
        long[][] dp = new long[bonusBeans.size()][yTime.size()];
        for (int i = 0; i <= dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++) {
                if (j == 0) {
                    dp[i][j] = bonusBeans.get(i).amount.longValue();
                } else {
                    dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j] + bonusBeans.get(i).amount.longValue());
                }
            }
        }

    }

    private long getTimeMillis(Time time) {
        return time.getHours() * 60 * 60 + time.getMinutes() * 60 + time.getSeconds();
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
