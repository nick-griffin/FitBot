from openai import OpenAI
import os
from dotenv import load_dotenv

load_dotenv()

client = OpenAI(
    base_url= 'https://api.groq.com/openai/v1',
    api_key = load_dotenv('API_KEY')
)

def get_user_info():
    print("Welcome to the FitBot! I am your AI coach. Please enter some important information to get started!")
    user = {}
    user['name'] = input("What is your name? ")
    user['age'] = input("Age: ")
    user['gender'] = input("Gender: ")
    user['height'] = input("Height (in feet and inches): ")
    user['goal_physique'] = input("What is your goal physique? (e.g., lose fat, gain muscle): ")
    user['weight'] = input("Weight (in lbs): ")
    user['goal_weight'] = input("Goal weight (in lbs): ")
    user['gym_access'] = input("Will you be working out at a gym or at home?: ")
    user['activity_level'] = input("Activity level (not active, lightly active, moderately active, very active): ")
    user['schedule'] = input("How many days a week do you want to work out? I reccomend not exceding 5 days per week \n as your body needs to rest to prevent injury: ")
    user['diet'] = input("What is your current diet like? (e.g., vegetarian, vegan, omnivore, etc.): ")
    user['injuries'] = input("Do you have any injuries or medical conditions I should be aware of? (yes/no): ").lower()
    if user['injuries'] == 'yes':
        user['injury_details'] = input("Please describe your injuries or medical conditions: ")
    else:
        user['injury_details'] = None
    return user

def ai_plan(user):
    prompt = f"""
    
You are a professional personal fitness and nutrition coach.

Create a 4 week personalized fitness and nutrition plan for the user based on the following information:

- Name: {user['name']}
- Age: {user['age']}
- Gender: {user['gender']}
- Height: {user['height']}
- Goal Physique: {user['goal_physique']}
- Current Weight: {user['weight']}
- Goal Weight: {user['goal_weight']}
- Gym Access: {user['gym_access']}
- Activity Level: {user['activity_level']}
- Workout Schedule: {user['schedule']}
- Diet Preference: {user['diet']}
- Injuries/Medical Conditions: {user['injury_details']}

Note:
- Calories may differ based on the specific ingredients and portion sizes.
- Refer to the user as "you" in the plan.
- Alter the sets and reps based on the user's fitness level and goal, like more weight and fewer reps, like 5-8 reps, for muscle gain on compund lifts, and lighter weight and more reps, like 10-15 reps, for fat loss.
- Include a variety of exercises to keep the workouts engaging.
- If the user doesn't have access to a gym, suggest bodyweight exercises or resistance bands.
- Format the exerceise plan in a clear and structured way like this: x reps, y sets, z rest time.
- Format the meal plan in a clear and structured way like this: Breakfast: x calories, y grams protein, z grams carbs, w grams fat.
- Give a breakdown of the macronutrients the user will be eating for the week.
- If the user is very active give more advanced exercises, if the user is not active give more beginner exercises.

Provide:
1. A weekly workout plan with exercises, sets, reps, and rest periods.
2. A daily meal plan, that includes breakfast, lunch, dinner, and one snack, with calorie counts and macronutrient breakdown.
3. Tips for staying motivated and tracking progress.
4. Tips to reduce iunjury risk based on the user's injuries or medical conditions.
"""
    return prompt

def get_ai_response(prompt):
    response = client.chat.completions.create(
        model = 'llama-3.1-8b-instant',
        messages = [
            {'role': 'system', 'content': 'You are a professional personal fitness and nutrition coach.'},
            {'role': 'user', 'content': prompt}
        ],
        temperature = 0.7,
        max_tokens = 1500
    )
    return response.choices[0].message.content.strip()

def main():
    user_info = get_user_info()
    plan = ai_plan(user_info)
    print('\nGenerating plan...\n')
    ai_response = get_ai_response(plan)
    print('Here is your personalized fitness and nutrition plan:\n', ai_response)

if __name__ == "__main__":
    main()