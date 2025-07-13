# Database Setup Guide

## 🔧 Fixing Database Connection Issues

If you're getting "Database driver not found" errors, follow these steps:

### 1. Check Project Structure
Make sure your project has the following structure:
```
WellnessSystem/
├── src/
│   ├── lib/
│   │   ├── derby.jar
│   │   ├── derbyshared.jar
│   │   └── derbytools.jar
│   ├── db/
│   │   ├── DBConnection.java
│   │   ├── CreateTables.java
│   │   ├── InsertSampleData.java
│   │   └── TestConnection.java
│   └── wellnesssystem/
```

### 2. Configure Classpath in NetBeans

1. **Right-click** on your project in NetBeans
2. Select **Properties**
3. Go to **Libraries** tab
4. Click **Add JAR/Folder**
5. Navigate to `src/lib/` and add all three JAR files:
   - `derby.jar`
   - `derbyshared.jar`
   - `derbytools.jar`
6. Click **OK** to save

### 3. Alternative: Copy JARs to Project Root

If the above doesn't work:
1. Copy the JAR files from `src/lib/` to your project root directory
2. Right-click on each JAR file in NetBeans
3. Select **Add to Classpath**

### 4. Test Database Connection

1. Run `TestConnection.java` to verify the connection works
2. You should see: "✅ Database connection successful!"

### 5. Setup Database

1. Run `CreateTables.java` to create the database tables
2. Run `InsertSampleData.java` to populate with sample data
3. Run `WellnessSystem.java` to start the application

### 6. Common Issues & Solutions

**Issue: "Database driver not found"**
- Solution: Ensure derby.jar is in the classpath (see steps 2-3 above)

**Issue: "Database connection failed"**
- Solution: Check if the database directory is writable
- Solution: Make sure no other application is using the database

**Issue: "Permission denied"**
- Solution: Run NetBeans as administrator
- Solution: Check folder permissions

### 7. Manual Database Creation

If automatic creation fails:
1. Create a folder named `wellnessDB` in your project root
2. Run the application again

### 8. Verify Setup

After setup, you should be able to:
- Login with sample credentials
- Access all interfaces
- Perform CRUD operations
- See data persist between sessions

## 🚀 Quick Start

1. **Configure classpath** (steps 2-3 above)
2. **Test connection**: Run `TestConnection.java`
3. **Setup database**: Run `CreateTables.java` then `InsertSampleData.java`
4. **Start application**: Run `WellnessSystem.java`
5. **Login**: Use sample credentials from README.md

## 📞 Troubleshooting

If you still have issues:
1. Check NetBeans console for detailed error messages
2. Verify all JAR files are properly added to classpath
3. Ensure you have write permissions in the project directory
4. Try running NetBeans as administrator

---

**Note**: The database will be created in your project directory as `wellnessDB/` folder. 